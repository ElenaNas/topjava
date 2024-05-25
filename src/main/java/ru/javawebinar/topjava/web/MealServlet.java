package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository.getFilteredAndSortedTos;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealService mealService;

    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        mealService = context.getBean(MealService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealService.create(SecurityUtil.authUserId(), meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                log.info("Delete id={}", getMealId(request));
                mealService.delete(SecurityUtil.authUserId(),getMealId(request));
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealService.get(SecurityUtil.authUserId(), getMealId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            case "filter":
                log.info("filter");
                String startDateStr = request.getParameter("startDate");
                LocalDate startDate = null;
                if (startDateStr != null && !startDateStr.isEmpty()) {
                    startDate = LocalDate.parse(startDateStr);
                }

                String startTimeStr = request.getParameter("startTime");
                LocalTime startTime = null;
                if (startTimeStr != null && !startTimeStr.isEmpty()) {
                    startTime = LocalTime.parse(startTimeStr);
                }

                String endDateStr = request.getParameter("endDate");
                LocalDate endDate = null;
                if (endDateStr != null && !endDateStr.isEmpty()) {
                    endDate = LocalDate.parse(endDateStr);
                }

                String endTimeStr = request.getParameter("endTime");
                LocalTime endTime = null;
                if (endTimeStr != null && !endTimeStr.isEmpty()) {
                    endTime = LocalTime.parse(endTimeStr);
                }

                List<MealTo> filteredAndSortedMeals;
                if (startDate == null && startTime == null && endDate == null && endTime == null) {
                    filteredAndSortedMeals = MealsUtil.getTos(mealService.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
                } else {
                    filteredAndSortedMeals = getFilteredAndSortedTos(
                            MealsUtil.getTos(mealService.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY),
                            startTime != null ? startTime : LocalTime.MIN,
                            startDate != null ? startDate : LocalDate.MIN,
                            endTime != null ? endTime : LocalTime.MAX,
                            endDate != null ? endDate : LocalDate.now()
                    );
                }

                request.setAttribute("meals", filteredAndSortedMeals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            default:
                log.info("Unknown action: {}", action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    private int getMealId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        log.info("Closing MealServlet and performing cleanup tasks...");

        super.destroy();
    }
}
