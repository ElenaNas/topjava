package ru.javawebinar.topjava.web.StringToLocalDateTimeConverter;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private final String datePattern;

    public StringToLocalDateConverter() {
        this.datePattern = DATE_PATTERN;
    }

    public StringToLocalDateConverter(String datePattern) {
        this.datePattern = datePattern;
    }

    @Override
    public LocalDate convert(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(datePattern));
    }
}