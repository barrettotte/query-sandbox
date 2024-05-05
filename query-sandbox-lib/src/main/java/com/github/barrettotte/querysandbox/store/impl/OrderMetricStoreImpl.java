package com.github.barrettotte.querysandbox.store.impl;

import com.github.barrettotte.querysandbox.store.OrderMetricStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMetricStoreImpl implements OrderMetricStore {
    @Autowired
    JdbcTemplate jdbc;

    // TODO:
}
