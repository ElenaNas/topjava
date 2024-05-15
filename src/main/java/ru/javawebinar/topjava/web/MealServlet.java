package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.model.MealData.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.model.MealData.MEAL_LIST;
import static ru.javawebinar.topjava.util.MealsUtil.convertToMealTo;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        //List<MealTo> mealToList = filteredByStreams(MEAL_LIST, LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59), CALORIES_PER_DAY);
        List<MealTo> mealToList = convertToMealTo(MEAL_LIST, CALORIES_PER_DAY);
        request.setAttribute("meals", mealToList);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}