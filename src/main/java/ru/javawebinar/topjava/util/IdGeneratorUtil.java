package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGeneratorUtil {
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public static Integer getNextId() {
        return idCounter.incrementAndGet();
    }
}
