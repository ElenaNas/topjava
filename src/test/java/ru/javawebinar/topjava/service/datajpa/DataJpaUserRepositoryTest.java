package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserRepositoryTest extends AbstractUserServiceTest {

    @Autowired
    UserService service;

    @Test
    public void getNotFoundUserWithMealsById() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getUserAndMealsById(NOT_FOUND));
    }

    @Test
    public void getUserAndMealsById() {
        User user = service.getUserAndMealsById(USER_ID);
        MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.meals);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }
}