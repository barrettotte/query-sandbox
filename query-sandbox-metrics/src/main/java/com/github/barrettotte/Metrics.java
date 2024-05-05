package com.github.barrettotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Metrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(Metrics.class);

    public static void main(String[] args) {
        LOGGER.debug("Starting metrics collection...");

        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        Runnable collectMetrics = () -> {
            try {
                LOGGER.info("METRICS COLLECT!");
            } catch (Exception e) {
                LOGGER.error("Error occurred while collecting metrics", e);
            }
        };

        // schedule metrics collector to run on an interval
        scheduledExecutor.scheduleAtFixedRate(collectMetrics, 0, 5, TimeUnit.MINUTES);

    }
}
