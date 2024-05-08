package com.github.barrettotte;

import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Metrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(Metrics.class);

    @Autowired
    private static OrderMetricsService orderMetricsService;
    // TODO: not working

    public static void main(String[] args) {
        LOGGER.debug("Starting metrics collection...");

        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        Runnable collectMetrics = () -> {
            try {
                orderMetricsService.collect();
            } catch (Exception e) {
                LOGGER.error("Error occurred while collecting metrics", e);
            }
        };

        // schedule metrics collector to run on an interval
        scheduledExecutor.scheduleAtFixedRate(collectMetrics, 0, 5, TimeUnit.MINUTES);

    }
}
