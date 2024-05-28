package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime localTime, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || localTime.compareTo(startTime) >= 0)
                && (endTime == null || localTime.compareTo(endTime) < 0);
    }

    public static boolean isWithinDateRange(LocalDateTime dateTime, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || dateTime.toLocalDate().compareTo(startDate) >= 0)
                && (endDate == null || dateTime.toLocalDate().compareTo(endDate) <= 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            return LocalDate.parse(dateStr);
        }
        return null;
    }

    public static LocalTime parseLocalTime(String timeStr) {
        if (timeStr != null && !timeStr.isEmpty()) {
            return LocalTime.parse(timeStr);
        }
        return null;
    }
}

