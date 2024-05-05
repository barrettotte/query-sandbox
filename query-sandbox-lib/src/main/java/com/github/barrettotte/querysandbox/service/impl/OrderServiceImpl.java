package com.github.barrettotte.querysandbox.service.impl;

import com.github.barrettotte.querysandbox.model.Order;
import com.github.barrettotte.querysandbox.service.OrderService;
import com.github.barrettotte.querysandbox.store.OrderStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderStore orderStore;

    @Override
    public List<Order> getAll() {
        LOGGER.debug("Fetching orders");
        return orderStore.getAll();
    }

    @Override
    public Optional<Order> getById(String id) {
        LOGGER.debug("Fetching order by ID {}", id);
        return orderStore.getById(id);
    }

    @Override
    public Order create(Order order) {
        LOGGER.debug("Creating order {}", order);
        try {
            return orderStore.create(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order update(Order original, Order toUpdate) {
        LOGGER.debug("Updating order => original={}, updated={}", original, toUpdate);
        try {
            return orderStore.update(original, toUpdate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        LOGGER.debug("Deleting order by ID {}", id);
        orderStore.deleteById(id);
    }
}
