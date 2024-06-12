package ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractJdbcMealRepository implements MealRepository {

    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected final SimpleJdbcInsert insertMeal;

    public AbstractJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meal")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (isNewMeal(meal)) {
            return insertMeal(meal, userId);
        } else {
            return updateMeal(meal, userId);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meal WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query(
                "SELECT * FROM meal WHERE id = ? AND user_id = ?", getRowMapper(), id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meal WHERE user_id=? ORDER BY date_time DESC", getRowMapper(), userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meal WHERE user_id=? AND date_time >= ? AND date_time < ? ORDER BY date_time DESC",
                getRowMapper(), userId, startDateTime, endDateTime);
    }

    protected RowMapper<Meal> getRowMapper() {
        return new MealRowMapper();
    }

    public static class MealRowMapper implements RowMapper<Meal> {
        @Override
        public Meal mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Meal meal = new Meal();
            meal.setId(resultSet.getInt("id"));
            meal.setDescription(resultSet.getString("description"));
            meal.setCalories(resultSet.getInt("calories"));
            meal.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());
            return meal;
        }
    }

    protected abstract boolean isNewMeal(Meal meal);

    protected abstract Meal insertMeal(Meal meal, int userId);

    protected abstract Meal updateMeal(Meal meal, int userId);
}

