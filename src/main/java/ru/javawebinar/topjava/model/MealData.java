package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.IdGeneratorUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MealData {

    public static final int CALORIES_PER_DAY = 2000;

    public static final int INITIAL_SIZE = 7;

    public static final Integer ID_1 = IdGeneratorUtil.getNextId();
    public static final Integer ID_2 = IdGeneratorUtil.getNextId();
    public static final Integer ID_3 = IdGeneratorUtil.getNextId();
    public static final Integer ID_4 = IdGeneratorUtil.getNextId();
    public static final Integer ID_5 = IdGeneratorUtil.getNextId();
    public static final Integer ID_6 = IdGeneratorUtil.getNextId();
    public static final Integer ID_7 = IdGeneratorUtil.getNextId();
    public static final Integer ID_8 = IdGeneratorUtil.getNextId();


    public static final Meal ONE = new Meal(ID_1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal TWO = new Meal(ID_2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal THREE = new Meal(ID_3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal FOUR = new Meal(ID_4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal FIVE = new Meal(ID_5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal SIX = new Meal(ID_6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal SEVEN = new Meal(ID_7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal EIGHT = new Meal(ID_8, LocalDateTime.now(), "Late snack", 300);

    public static List<Meal> MEAL_LIST = new ArrayList<>();

    static {
        MEAL_LIST.add(ONE);
        MEAL_LIST.add(TWO);
        MEAL_LIST.add(THREE);
        MEAL_LIST.add(FOUR);
        MEAL_LIST.add(FIVE);
        MEAL_LIST.add(SIX);
        MEAL_LIST.add(SEVEN);
    }
}
