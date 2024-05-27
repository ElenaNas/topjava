package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class UserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private ApplicationContext context;
    private MealRestController mealRestController;

    @Override
    public void init() {
      context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
      mealRestController=context.getBean(MealRestController.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(userId);

        List<MealTo> mealList = mealRestController.getAll();
        request.setAttribute("meals", mealList);
        response.sendRedirect(request.getContextPath() + "/meals");
    }
}