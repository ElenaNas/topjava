package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isBetweenHalfOpenForFilter(LocalDateTime ldt, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return ldt.compareTo(startDateTime) >= 0 && ldt.compareTo(endDateTime) < 0;
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

