package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpenForFilter;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDate);

    {
        for (Meal meal : MealsUtil.meals) {
            save(1, meal);
        }
    }

    @Override
    public synchronized Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(meal.getId(), meal);

        return meal;
    }

    @Override
    public synchronized boolean delete(int userId, int id) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.values().stream()
                    .sorted(MEAL_COMPARATOR.reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static List<MealTo> getFilteredAndSortedTos(List<MealTo> meals, LocalTime startTime, LocalDate startDate, LocalTime endTime, LocalDate endDate) {
        return meals.stream()
                .filter(meal -> isBetweenHalfOpenForFilter(meal.getDateTime(), startDate, endDate, startTime, endTime))
                .sorted(Comparator.comparing(MealTo::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

