package ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class HsqldbJdbcMealRepositoryTest extends AbstractMealServiceTest {

}