package com.github.barrettotte.querysandbox.collect;

import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MetricsCollector implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsApplication.class);

    @Autowired
    OrderMetricsService orderMetricsService;

    @Override
    public void run(String... args) throws Exception {
        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);

        Runnable collectMetrics = () -> {
            try {
                orderMetricsService.collect();
            } catch (Exception e) {
                LOGGER.error("Error occurred while collecting metrics", e);
                System.exit(1);
            }
        };

        // schedule metrics collector to run on an interval
        scheduledExecutor.scheduleAtFixedRate(collectMetrics, 5, 5, TimeUnit.MINUTES);
    }
}
