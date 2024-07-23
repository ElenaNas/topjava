package ru.javawebinar.topjava.web.user;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;


@Component
public class EmailValidator implements Validator {

    private final UserRepository repository;

    public EmailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasEmail user = ((HasEmail) target);
        User newUser = repository.getByEmail(user.getEmail());
        if (newUser != null) {
            if (user.getId() != null) {
                int id = newUser.id();
                if (user.getId() != null && id == user.id()){
                    return;
                }
                errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
            }
        }
    }
}

