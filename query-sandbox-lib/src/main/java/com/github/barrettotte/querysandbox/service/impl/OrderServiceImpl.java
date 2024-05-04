package com.github.barrettotte.querysandbox.service.impl;

import com.github.barrettotte.querysandbox.model.Order;
import com.github.barrettotte.querysandbox.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public List<Order> getAll() {
        LOGGER.debug("Fetching asteroids");
        // TODO: store getAll
        return List.of();
    }

    @Override
    public Optional<Order> getById(String id) {
        LOGGER.debug("Fetching asteroid by ID {}", id);
        // TODO: store getById
        return Optional.empty();
    }

    @Override
    public Order create(Order order) {
        // set generated values that should not be accepted from user
        order.setId(String.valueOf(UUID.randomUUID()));
        order.setCreated(Instant.now());
        order.setUpdated(order.getCreated());

        LOGGER.debug("Creating asteroid {}", order);
        // TODO: store create
        return null;
    }

    @Override
    public Order update(Order original, Order toUpdate) {
        // TODO: store update
        Order merged = merge(original, toUpdate);
        LOGGER.debug("Updating asteroid => original={}, updated={}", original, merged);
        return null;
    }

    @Override
    public void deleteById(String id) {
        LOGGER.debug("Deleting asteroid by ID {}", id);
        // TODO: store delete
    }

    /**
     * Merge an update into an order
     * @param original original order to update
     * @param update order to merge into original order
     * @return merged order
     */
    private Order merge(Order original, Order update) {
        return new Order (
                original.getId(),
                original.getCreated(),
                Instant.now(),
                Optional.ofNullable(update.getDescription()).orElse(original.getDescription()),
                Optional.ofNullable(update.getStatus()).orElse(original.getStatus()),
                Optional.ofNullable(update.getCategory()).orElse(original.getCategory()),
                Optional.ofNullable(update.getPriority()).orElse(original.getPriority()),
                Optional.ofNullable(update.getOrderType()).orElse(original.getOrderType()),
                Optional.ofNullable(update.getHidden()).orElse(original.getHidden()),
                Optional.ofNullable(update.getAssigneeIds()).orElse(original.getAssigneeIds())
        );
    }
}
