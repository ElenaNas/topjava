package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("Filtered by Streams " + filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println("Filtered by Streams in one go " + filteredByStreamsInOneGo(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        if (mealsTo != null) {
            mealsTo.forEach(System.out::println);
        } else {
            System.out.println("filteredByCycles: No meals found.");
        }
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = new HashMap<>();
        List<UserMealWithExcess> mealWithExcess = new ArrayList<>();

        for (UserMeal meal : meals) {
            LocalDate mealDay = meal.getDateTime().toLocalDate();
            dayCalories.put(mealDay, dayCalories.getOrDefault(mealDay, 0) + meal.getCalories());
        }

        for (UserMeal meal : meals) {
            LocalDate mealDay = meal.getDateTime().toLocalDate();
            int totalCalories = dayCalories.getOrDefault(mealDay, 0);

            if (totalCalories > caloriesPerDay && TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcess.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        true));
            }
        }

        return mealWithExcess.isEmpty() ? null : mealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> {
                    LocalDate mealDay = meal.getDateTime().toLocalDate();
                    int totalCalories = dayCalories.getOrDefault(mealDay, 0);
                    return totalCalories > caloriesPerDay && TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime);
                })
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        true))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsInOneGo(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                mealsList -> {
                                    int totalCalories = mealsList.stream().mapToInt(UserMeal::getCalories).sum();
                                    if (totalCalories > caloriesPerDay) {
                                        return mealsList.stream()
                                                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                                                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true))
                                                .collect(Collectors.toList());
                                    } else {
                                        return Collections.<UserMealWithExcess>emptyList();
                                    }
                                }
                        )
                ))
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}