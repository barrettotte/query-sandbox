package com.github.barrettotte.querysandbox.store.impl;

import com.github.barrettotte.querysandbox.store.OrderStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderStoreImpl implements OrderStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStoreImpl.class);

    @Autowired
    JdbcTemplate jdbc;
}
