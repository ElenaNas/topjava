package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestNameAndTimeLogger implements TestRule {

    private static final Logger LOGGER = Logger.getLogger(TestNameAndTimeLogger.class.getName());

    private final Map<String, Long> testTimes = new HashMap<>();

    static {
        try {
            FileHandler fileHandler = new FileHandler("test.log");
            LOGGER.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            LOGGER.warning("Failed to initialize file handler for logging: " + e.getMessage());
        }
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.currentTimeMillis();
                try {
                    statement.evaluate();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;
                    testTimes.put(description.getMethodName(), elapsedTime);
                    LOGGER.info(description.getMethodName() + ": " + elapsedTime + "ms");
                }
            }
        };
    }

    public void logSummary() {
        LOGGER.info("\nTest Summary:");
        testTimes.forEach((testName, timeTaken) -> LOGGER.info(testName + ": " + timeTaken + "ms"));
    }
}
