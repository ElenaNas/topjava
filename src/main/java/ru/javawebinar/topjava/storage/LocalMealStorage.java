package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class LocalMealStorage implements MealStorage {

    private static final Logger log = Logger.getLogger(LocalMealStorage.class.getName());

    private final Map<Integer, Meal>mealMap=new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("Save " + meal);
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
        }
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) ->meal);
    }

    @Override
    public Meal get(int id) {
        log.info("Get " + id);
        return mealMap.get(id);
    }

    @Override
    public void delete(int id) {
        log.info("Delete " + id);
        mealMap.remove(id);
    }

    @Override
    public Collection<Meal> getAll() {
        log.info("GetAll");
        return mealMap.values();
    }
}
