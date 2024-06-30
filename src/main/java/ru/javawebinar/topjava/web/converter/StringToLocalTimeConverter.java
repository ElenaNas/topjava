package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    private static final String TIME_PATTERN = "HH:mm";

    @Override
    public LocalTime convert(String timeString) {
        if (timeString.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }
}
