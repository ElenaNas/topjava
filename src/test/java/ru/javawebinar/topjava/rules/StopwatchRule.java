package ru.javawebinar.topjava.rules;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class StopwatchRule extends TestWatcher {

    private static final Logger log = getLogger(StopwatchRule.class);

    private long startTime;

    @Override
    protected void starting(Description description) {
        startTime = System.nanoTime();
    }

    @Override
    protected void finished(Description description) {
        long endTime = System.nanoTime();
        logInfo(description.getMethodName(), endTime - startTime);
    }

    private void logInfo(String testName, long nanos) {
        log.info(String.format("Test %s %s, spent %d microseconds",
                testName, "succeeded", TimeUnit.NANOSECONDS.toMicros(nanos)));
    }
}

