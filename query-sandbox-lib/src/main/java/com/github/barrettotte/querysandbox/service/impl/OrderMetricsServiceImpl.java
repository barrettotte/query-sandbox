package com.github.barrettotte.querysandbox.service.impl;

import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import com.github.barrettotte.querysandbox.store.OrderMetricsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMetricsServiceImpl implements OrderMetricsService {

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

    }
}
