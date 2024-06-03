package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_1_ID = START_SEQ + 3;
    public static final int MEAL_2_ID = START_SEQ + 4;
    public static final int MEAL_3_ID = START_SEQ + 5;
    public static final int MEAL_4_ID = START_SEQ + 6;
    public static final int MEAL_5_ID = START_SEQ + 7;
    public static final int MEAL_6_ID = START_SEQ + 8;

    public static final int NOT_FOUND = 10;

    public static final Meal meal1 = new Meal(MEAL_1_ID, LocalDateTime.of(2024, Month.JUNE, 1, 8, 0), "Breakfast", 400);
    public static final Meal meal2 = new Meal(MEAL_2_ID, LocalDateTime.of(2024, Month.JUNE, 1, 10, 0), "Second Breakfast", 150);
    public static final Meal meal3 = new Meal(MEAL_3_ID, LocalDateTime.of(2024, Month.JUNE, 1, 12, 30), "Lunch", 600);
    public static final Meal meal4 = new Meal(MEAL_4_ID, LocalDateTime.of(2024, Month.JUNE, 1, 16, 30), "Afternoon snack", 100);
    public static final Meal meal5 = new Meal(MEAL_5_ID, LocalDateTime.of(2024, Month.JUNE, 1, 18, 30), "Dinner", 600);
    public static final Meal meal6 = new Meal(MEAL_6_ID, LocalDateTime.of(2024, Month.JUNE, 1, 21, 30), "Late night snack", 100);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, Month.JANUARY, 1, 10, 30), "Snack", 300);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_1_ID, meal1.getDateTime(), meal1.getDescription(), meal1.getCalories());
        updated.setDateTime(LocalDateTime.of(2024, Month.JUNE, 1, 9, 10));
        updated.setDescription("Second breakfast");
        updated.setCalories(300);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }
}
