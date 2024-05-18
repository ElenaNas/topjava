import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.IStorage;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javawebinar.topjava.model.MealData.*;

public abstract class AbstractStorageTest {
    protected IStorage storage;

    public AbstractStorageTest(IStorage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() throws Exception {
        clear();
        storage.save(ONE);
        storage.save(TWO);
        storage.save(THREE);
        storage.save(FOUR);
        storage.save(FIVE);
        storage.save(SIX);
        storage.save(SEVEN);
        System.out.println("Storage reloaded. It contains 3 meals");
    }

    @Test
    public void size() {
        assertSize(INITIAL_SIZE);
        System.out.println("Storage size is " + INITIAL_SIZE + ".");
    }

    @Test
    public void get() throws Exception {
        assertGet(ONE);
        System.out.println("Here's the meal you are looking for: " + ONE);
        assertGet(TWO);
        System.out.println("Here's the meal you are looking for: " + TWO);
        assertGet(THREE);
        System.out.println("Here's the meal you are looking for: " + THREE);
    }


    @Test
    public void getAllSorted() {
        List<Meal> list = storage.getAllSorted();
        assertEquals(7, list.size());
        assertEquals(list, Arrays.asList(ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN));
        System.out.println("All meals from the storage are listed below:\n" + storage.getAllSorted());
    }

    @Test
    public void update() throws Exception {
        Meal meal = new Meal(ID_1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        storage.update(meal);
        assertEquals(meal, storage.get(ONE.getId()));
        System.out.println("Meal " + meal.getId() + " has been updated.");
    }

    @Test
    public void clear()  {
        storage.clear();
        assertSize(0);
        System.out.println("Storage has been cleared, current size is " + 0);
    }

    @Test
    public void save() throws Exception {
        assertSize(INITIAL_SIZE);
        storage.save(EIGHT);
        assertSize(INITIAL_SIZE + 1);
        assertGet(EIGHT);
        System.out.println("Meal " + EIGHT + " has been added.");
    }

    @Test
    public void delete() throws Exception {
        storage.delete(ID_2);
        assertSize(INITIAL_SIZE - 1);
        System.out.println("Meal " + TWO + " has been deleted.");
        System.out.println("Storage size is " + (INITIAL_SIZE - 1) + ".");
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Meal meal) throws Exception {
        assertEquals(meal, storage.get(meal.getId()));
    }
}
