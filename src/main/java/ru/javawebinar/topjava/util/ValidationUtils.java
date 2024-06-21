package ru.javawebinar.topjava.util;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import java.util.Set;

public class ValidationUtils {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static ExecutableValidator executableValidator() {
        return validator.forExecutables();
    }
}