package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ResponseEntityErrorHandling {
    public static ResponseEntity<String> handleBindingResultErrors(BindingResult result) {
        if (result.hasErrors()) {
            String errorFieldsMsg = result.getFieldErrors().stream()
                    .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                    .collect(Collectors.joining("<br>"));
            return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
        }
        return null;
    }
}
