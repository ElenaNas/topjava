package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    private final MealService service;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> filter(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("filter");
        List<Meal> mealsDateFiltered=service.getFilteredByDates(startDate, endDate, userId);
        return MealsUtil.getFilteredAndSortedTos(mealsDateFiltered, startTime, startDate, endTime, endDate);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {}", id);
        return service.get(userId, id);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {}", id);
        service.delete(userId, id);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }

    public List<MealTo> getTimeDateFilteredMeals(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        if (startDate == null && startTime == null && endDate == null && endTime == null) {
            return getAll();
        } else {
            return filter(
                    startDate != null ? startDate : LocalDate.MIN,
                    startTime != null ? startTime : LocalTime.MIN,
                    endDate != null ? endDate : LocalDate.MAX,
                    endTime != null ? endTime : LocalTime.MAX
            );
        }
    }
}