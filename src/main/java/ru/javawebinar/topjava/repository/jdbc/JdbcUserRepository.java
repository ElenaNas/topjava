package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.Types;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        parameterSource.registerSqlType("roles", Types.ARRAY);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay " +
                            "WHERE id=:id", parameterSource);
        }
        updateRoles(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, ur.role FROM users u " +
                        "LEFT JOIN user_role ur ON u.id = ur.user_id " +
                        "WHERE u.id=?",
                (resultSet, rowNum) -> {
                    User user = ROW_MAPPER.mapRow(resultSet, rowNum);
                    String role = resultSet.getString("role");
                    if (role != null) {
                        assert user != null;
                        user.getRoles().add(Role.valueOf(role));
                    }
                    return user;
                }, id);

        if (users.isEmpty()) {
            return null;
        } else {
            User user = users.getFirst();
            if (users.size() > 1) {
                for (int i = 1; i < users.size(); i++) {
                    user.getRoles().addAll(users.get(i).getRoles());
                }
            }
            return user;
        }
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_role WHERE user_id=?", Role.class, user.getId());
            user.setRoles(new HashSet<>(roles));
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, List<Role>> userRolesMap = new HashMap<>();
        jdbcTemplate.query(
                "SELECT user_id, role FROM user_role",
                (resultSet, rowNum) -> {
                    int userId = resultSet.getInt("user_id");
                    Role role = Role.valueOf(resultSet.getString("role"));
                    userRolesMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(role);
                    return null;
                });
        users.forEach(user -> {
            List<Role> roles = userRolesMap.get(user.getId());
            user.setRoles(Objects.requireNonNullElse(roles, Collections.emptyList()));
        });
        return users;
    }

    public void updateRoles(User user) {
        namedParameterJdbcTemplate.update("DELETE FROM user_role WHERE user_id = :userId",
                Collections.singletonMap("userId", user.getId()));

        List<MapSqlParameterSource> batch = user.getRoles().stream()
                .map(role -> new MapSqlParameterSource("userId", user.getId()).addValue("role", role.name()))
                .toList();

        namedParameterJdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (:userId, :role)", batch.toArray(new MapSqlParameterSource[0]));
    }
}