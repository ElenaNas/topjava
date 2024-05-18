package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.IStorage;
import ru.javawebinar.topjava.storage.ListStorage;
import ru.javawebinar.topjava.util.IdGeneratorUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.model.MealData.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.model.MealData.MEAL_LIST;
import static ru.javawebinar.topjava.util.MealsUtil.convertToMealToWithId;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String PARAM_ACTION = "action";
    private static final String PARAM_ID = "id";

    IStorage storage;

    private final Object storageLock = new Object();

    @Override
    public void init() throws ServletException {
        super.init();
        synchronized (storageLock) {
            storage = new ListStorage();
            MEAL_LIST = storage.getAllSorted();
        }
    }

    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        Integer id = null;
        String action = request.getParameter(PARAM_ACTION);

        if (action != null && !action.isEmpty()) {
            String idParam = request.getParameter(PARAM_ID);
            if (idParam != null && !idParam.isEmpty()) {
                id = Integer.valueOf(idParam);
            }
            switch (action) {
                case "delete":
                    try {
                        synchronized (storageLock) {
                            storage.delete(id);
                            MEAL_LIST = storage.getAllSorted();
                        }
                        response.sendRedirect(request.getContextPath() + "/meals");
                        return;
                    } catch (Exception e) {
                        log.error("Failed to delete meal", e);
                    }
                case "add":
                case "edit":
                    try {
                        Meal meal;
                        synchronized (storageLock) {
                            if (id == null || id == 0) {
                                meal = new Meal(LocalDateTime.now(), "", 0);
                            } else {
                                meal = storage.get(id);
                                if (meal == null) {
                                    meal = new Meal(LocalDateTime.now(), "", 0);
                                }
                            }
                        }
                        request.setAttribute("meals", meal);
                        request.getRequestDispatcher("/WEB-INF/edit.jsp").forward(request, response);
                        return;
                    } catch (Exception e) {
                        log.error("An error while editing meal", e);
                    }
            }
        }
        List<MealTo> mealToList = convertToMealToWithId(MEAL_LIST, CALORIES_PER_DAY);
        request.setAttribute("meals", mealToList);
        request.getRequestDispatcher("/WEB-INF/meals.jsp").forward(request, response);
    }

    @Override
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter(PARAM_ACTION);
        int id = 0;

        String idParam = request.getParameter(PARAM_ID);
        if (idParam != null && !idParam.isEmpty()) {
            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                log.error("Error while parsing id", e);
                return;
            }
        }

        Meal meal;
        switch (action) {
            case "add":
            case "edit":
                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
                String description = request.getParameter("description");
                int calories = Integer.parseInt(request.getParameter("calories"));

                synchronized (storageLock) {
                    if (id != 0) {
                        try {
                            meal = storage.get(id);
                            meal.setDateTime(dateTime);
                            meal.setDescription(description);
                            meal.setCalories(calories);
                            storage.update(meal);
                        } catch (Exception e) {
                            log.error("Error while editing meal", e);
                        }
                    } else {
                        meal = new Meal(IdGeneratorUtil.getNextId(), dateTime, description, calories);
                        try {
                            storage.save(meal);
                            storage.update(meal);
                        } catch (Exception e) {
                            log.error("Error while saving meal", e);
                        }
                    }
                    MEAL_LIST = storage.getAllSorted();
                }
                List<MealTo> mealToList = convertToMealToWithId(MEAL_LIST, CALORIES_PER_DAY);
                request.setAttribute("meals", mealToList);
                response.sendRedirect(request.getContextPath() + "/meals");
                return;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }
}



