package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface IStorage {
    void clear();

    void update(Meal meal) throws Exception;

    void save(Meal meal) throws Exception;

    Meal get(Integer id) throws Exception;

    void delete(Integer id) throws Exception;

    int size();

    List<Meal> getAllSorted();
}
