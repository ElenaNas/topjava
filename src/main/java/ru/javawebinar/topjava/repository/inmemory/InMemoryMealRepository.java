package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDate);

    {
        for (Meal meal : MealsUtil.meals1) {
            save(1, meal);
        }
    }

    {
        for (Meal meal : MealsUtil.meals2) {
            save(2, meal);
        }
    }

    @Override
    public Meal save(int userId, Meal meal) {
        meal.setId(meal.isNew() ? counter.getAndIncrement() : userId);
        repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null ? userMeals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null ? userMeals.values().stream()
                .sorted(MEAL_COMPARATOR.reversed())
                .collect(Collectors.toList()) : Collections.emptyList();
    }
}

