package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping()
    public String saveOrUpdate(HttpServletRequest request) {
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
            log.info("Creating new meal: {}", meal);
            super.create(meal);
        } else {
            int id = getId(request);
            log.info("Updating meal with ID {}: {}", id, meal);
            super.update(meal, id);
        }
        log.info("Redirecting to /meals after save/update operation");
        return "redirect:/meals";
    }

    @GetMapping("/delete")
    private String delete(HttpServletRequest request) {
        int id = getId(request);
        log.info("Deleting meal with ID: {}", id);
        super.delete(id);
        log.info("Redirecting to /meals after deleting meal with ID: {}", id);
        return "redirect:/meals";
    }

    @GetMapping("/update")
    public String showUpdateForm(HttpServletRequest request, Model model) {
        int id = getId(request);
        log.info("Fetching meal details for update with ID: {}", id);
        Meal meal = super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("Showing create form for new meal");
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        log.info("Filtering meals between {} {} and {} {}", startDate, startTime, endDate, endTime);
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = request.getParameter("id");
        return Integer.parseInt(paramId);
    }
}