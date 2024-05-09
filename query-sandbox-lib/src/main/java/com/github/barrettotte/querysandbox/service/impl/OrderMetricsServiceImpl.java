package com.github.barrettotte.querysandbox.service.impl;

import com.github.barrettotte.querysandbox.metrics.*;
import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import com.github.barrettotte.querysandbox.store.OrderMetricsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderMetricsServiceImpl implements OrderMetricsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMetricsService.class);

    @Autowired
    private OrderMetricsStore orderMetricsStore;

    @Override
    public Map<String, Long> distribution(DistributionSearch search) {
        LOGGER.debug("Fetching distribution using {}", search);
        return orderMetricsStore.distribution(search);
    }

    @Override
    public List<TrendBucket> trends(TrendsSearch search) {
        LOGGER.debug("Fetching trends");
        return orderMetricsStore.trends(search);
    }

    @Override
    public DeltaResult delta(DateRangeSearch search) {
        LOGGER.debug("Fetching deltas");
        return orderMetricsStore.delta(search);
    }

    @Override
    public void collect() {
        LOGGER.debug("Collecting metrics...");
        orderMetricsStore.collect();
        LOGGER.debug("Done collecting metrics.");
    }
}
