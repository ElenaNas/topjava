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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository.convertAndFilter;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

   /* private MealRepository repository;

    @Override
    public void init() {
        repository = new InMemoryMealRepository();
    }*/

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
            case "filter":
                LocalDate startDate = null;
                LocalDate endDate = null;
                LocalTime startTime = null;
                LocalTime endTime = null;

                // Parse parameters for filtering
                try {
                    startDate = LocalDate.parse(request.getParameter("startDate"));
                    endDate = LocalDate.parse(request.getParameter("endDate"));
                    startTime = LocalTime.parse(request.getParameter("startTime"));
                    endTime = LocalTime.parse(request.getParameter("endTime"));
                } catch (DateTimeParseException e) {
                    // Handle parsing errors, if any
                    // You might want to log or inform the user about invalid input
                }

                // Filter and convert meals
                List<MealTo> filteredMeals = convertAndFilter(
                        mealService.getAll(SecurityUtil.authUserId()), startDate, endDate, startTime, endTime);

                request.setAttribute("meals", filteredMeals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        MealsUtil.getTos(mealService.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
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
