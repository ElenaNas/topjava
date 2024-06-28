package ru.javawebinar.topjava.web.StringToLocalDateTimeConverter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    private static final String DEFAULT_TIME_PATTERN = "HH:mm";
    private final String timePattern;

    public StringToLocalTimeConverter() {
        this.timePattern = DEFAULT_TIME_PATTERN;
    }

    public StringToLocalTimeConverter(String timePattern) {
        this.timePattern = timePattern;
    }

    @Override
    public LocalTime convert(String timeString) {
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern(timePattern));
    }
}
