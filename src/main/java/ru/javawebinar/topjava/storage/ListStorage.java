package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.javawebinar.topjava.model.MealData.MEAL_LIST;

public class ListStorage extends AbstractStorage<Integer> {

    protected final List<Meal> mealList = new CopyOnWriteArrayList<>(MEAL_LIST);

    @Override
    public synchronized Integer getSearchKey(Integer id) {
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected synchronized boolean isExisting(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    protected synchronized void doSave(Meal meal, Integer searchKey) {
        mealList.add(meal);
    }

    @Override
    protected synchronized Meal doGet(Integer id) {
        return mealList.get(id);
    }

    @Override
    protected synchronized void doDelete(Integer searchKey) {
        mealList.remove((int) searchKey);
    }

    @Override
    protected synchronized void doUpdate(Meal meal, Integer searchKey) {
        mealList.set(searchKey, meal);
    }

    @Override
    protected synchronized CopyOnWriteArrayList<Meal> doCopy() {
        return new CopyOnWriteArrayList<>(mealList);
    }

    @Override
    public synchronized void clear() {
        mealList.clear();
    }

    @Override
    public synchronized int size() {
        return mealList.size();
    }
}
