package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.LocalMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealStorage storage;

    @Override
    public void init() {
        storage = new LocalMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("redirect to meals");

        String action = request.getParameter("action");
        int id = 0;

        switch (action == null ? "view" : action) {
            case "delete":
                log.info("Delete {}", id);
                id = Integer.parseInt(request.getParameter("id"));
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
            case "edit":
                log.info("Add or Edit {}", id);
                if ("edit".equals(action)) {
                    id = Integer.parseInt(request.getParameter("id"));
                }
                Meal meal = id == 0 ? new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0) : storage.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("WEB-INF/editMeal.jsp").forward(request, response);
                return;
            case "view":
            default:
                log.info("getAll");
                request.setAttribute("meals", MealsUtil.convertToMealTo(storage.getAll(), CALORIES_PER_DAY));
                request.getRequestDispatcher("WEB-INF/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");

        int calories = 0;
        try {
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (NumberFormatException e) {
            throw new ServletException("Calories parameter is not a valid integer", e);
        }

        Meal meal;
        if (!id.isEmpty()) {
            log.info("Edit a meal");
            meal = storage.get(Integer.parseInt(id));
            storage.save(meal);
        } else {
            log.info("Add a meal");
            meal = new Meal(dateTime.truncatedTo(ChronoUnit.MINUTES), description, calories);
            storage.save(meal);
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}



