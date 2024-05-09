package com.github.barrettotte.querysandbox.service.impl;

import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import com.github.barrettotte.querysandbox.store.OrderMetricsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMetricsServiceImpl implements OrderMetricsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMetricsService.class);

    @Autowired
    private OrderMetricsStore orderMetricsStore;

    @Override
    public void distribution() {

    }

    @Override
    public void trends() {

    }

    @Override
    public void delta() {

    }

    @Override
    public void collect() {
        LOGGER.debug("Collecting metrics...");
        orderMetricsStore.collect();
        LOGGER.debug("Done collecting metrics.");
    }
}
