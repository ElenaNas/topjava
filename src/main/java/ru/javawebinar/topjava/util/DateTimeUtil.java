package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isWithinDateAndTimeRange(Meal meal, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        LocalDate mealDate = meal.getDateTime().toLocalDate();
        LocalTime mealTime = meal.getDateTime().toLocalTime();
        return mealDate.compareTo(startDate) >= 0 && mealDate.compareTo(endDate) <= 0 &&
                mealTime.compareTo(startTime) >= 0 && mealTime.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

