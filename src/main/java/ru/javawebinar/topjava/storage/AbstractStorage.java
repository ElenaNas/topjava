package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements IStorage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator
            .comparing(Meal::getId)
            .thenComparing(Meal::getDate)
            .thenComparing(Meal::getTime)
            .thenComparing(Meal::getCalories)
            .thenComparing(Meal::getDescription);

    @Override
    public void update(Meal meal) throws Exception {
        LOG.info("Update " + meal);
        SK searchKey = getExistingSearchKey(meal.getId());
        doUpdate(meal, searchKey);
    }

    @Override
    public void save(Meal meal) throws Exception {
        LOG.info("Save " + meal);
        SK searchKey = getNotExistingSearchKey(meal.getId());
        doSave(meal, searchKey);
    }

    @Override
    public Meal get(Integer id) throws Exception {
        LOG.info("Get " + id);
        SK searchKey = getExistingSearchKey(id);
        return doGet(searchKey);
    }

    @Override
    public void delete(Integer id) throws Exception {
        LOG.info("Delete " + id);
        SK searchKey = getExistingSearchKey(id);
        doDelete(searchKey);
    }

    @Override
    public List<Meal> getAllSorted() {
        LOG.info("GetAllSorted");
        List<Meal> list = doCopy();
        list.sort(MEAL_COMPARATOR);
        return list;
    }

    private SK getExistingSearchKey(Integer id) throws Exception {
        SK searchKey = getSearchKey(id);
        if (!isExisting(searchKey)) {
            LOG.warning("Meal " + id + " does not exist");
            throw new Exception("Meal does not exist");
        }
        return searchKey;
    }

    private SK getNotExistingSearchKey(Integer id) throws Exception {
        SK searchKey = getSearchKey(id);
        if (isExisting(searchKey)) {
            LOG.warning("Meal " + id + " already exists");
            throw new Exception("This meal already exists");
        }
        return searchKey;
    }

    protected abstract SK getSearchKey(Integer id);

    protected abstract boolean isExisting(SK searchKey);

    protected abstract void doSave(Meal meal, SK searchKey);

    protected abstract Meal doGet(SK id);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(Meal meal, SK searchKey);

    protected abstract CopyOnWriteArrayList<Meal> doCopy();
}
