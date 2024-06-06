package ru.javawebinar.topjava.rules;

import org.junit.AfterClass;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class SummaryRule implements TestRule {

    private static final Logger log = getLogger(SummaryRule.class);
    private static final Map<String, Long> testExecutionTimes = new HashMap<>();

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.nanoTime();
                try {
                    base.evaluate();
                } finally {
                    long endTime = System.nanoTime();
                    testExecutionTimes.put(description.getMethodName(), endTime - startTime);
                }
            }
        };
    }

    @AfterClass
    public static void logSummary() {
        log.info("Test Summary:");
        long totalExecutionTime = 0;
        for (Long executionTime : testExecutionTimes.values()) {
            totalExecutionTime += executionTime;
        }
        log.info(String.format("Total execution time for all tests: %d microseconds",
                TimeUnit.NANOSECONDS.toMicros(totalExecutionTime)));
    }
}
