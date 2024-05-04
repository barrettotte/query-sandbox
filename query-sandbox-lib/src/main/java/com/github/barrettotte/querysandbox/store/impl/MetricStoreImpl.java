package com.github.barrettotte.querysandbox.store.impl;

import com.github.barrettotte.querysandbox.store.MetricStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class MetricStoreImpl implements MetricStore {
    @Autowired
    JdbcTemplate jdbc;
}
