package ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

@Repository
@Profile(Profiles.POSTGRES_DB)
public class PostgresJdbcMealRepository extends AbstractJdbcMealRepository {

    public PostgresJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected boolean isNewMeal(Meal meal) {
        return meal.getId() == null;
    }

    @Override
    protected Meal insertMeal(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", meal.getDateTime())
                .addValue("user_id", userId);

        Number newId = insertMeal.executeAndReturnKey(map);

        meal.setId(newId.intValue());
        return meal;
    }

    @Override
    protected Meal updateMeal(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", meal.getDateTime())
                .addValue("user_id", userId);

        int rowsUpdated = namedParameterJdbcTemplate.update(
                "UPDATE meal SET description=:description, calories=:calories, date_time=:date_time WHERE id=:id AND user_id=:user_id", map);

        return rowsUpdated == 0 ? null : meal;
    }
}

