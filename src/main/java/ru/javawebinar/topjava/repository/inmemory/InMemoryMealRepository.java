package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
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

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    private static final Comparator<Meal> DATE_DESCENDING_COMPARATOR = Comparator.comparing(Meal::getDate).reversed();

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
                    .sorted(DATE_DESCENDING_COMPARATOR)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static List<MealTo> convertAndFilter(List<Meal> mealList, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        if (mealList.isEmpty()) {
            return Collections.emptyList();
        }

        if (startDate == null && endDate == null && startTime == null && endTime == null) {
            return mealList.stream()
                    .map(meal -> MealsUtil.createTo(meal, false))
                    .collect(Collectors.toList());
        }

        LocalDate startD = startDate != null ? startDate : LocalDate.now();
        LocalDate endD = endDate != null ? endDate : LocalDate.now();
        LocalTime startT = startTime != null ? startTime : LocalTime.MIDNIGHT;
        LocalTime endT = endTime != null ? endTime : LocalTime.MIDNIGHT;

        return mealList.stream()
                .filter(meal -> DateTimeUtil.isWithinDateAndTimeRange(meal, startD, endD, startT, endT))
                .sorted(DATE_DESCENDING_COMPARATOR)
                .map(meal -> MealsUtil.createTo(meal, false))
                .collect(Collectors.toList());
    }
}

