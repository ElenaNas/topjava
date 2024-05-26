package ru.javawebinar.topjava.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.meal.MealRestController;

@Configuration
@ComponentScan(basePackages = "ru.javawebinar.topjava.service")
public class SpringConfiguration {

    @Bean
    public InMemoryMealRepository mealRepository() {
        return new InMemoryMealRepository();
    }

    @Bean
    public InMemoryUserRepository userRepository(){
        return new InMemoryUserRepository();
    }

    @Bean
    public MealService mealService(MealRepository mealRepository) {
        return new MealService(mealRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository){
        return new UserService(userRepository);
    }

    @Bean
    public MealRestController mealRestController(MealService mealService){
        return new MealRestController(mealService);
    }

}
