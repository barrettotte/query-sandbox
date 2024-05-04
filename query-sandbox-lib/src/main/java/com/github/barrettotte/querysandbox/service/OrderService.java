package com.github.barrettotte.querysandbox.service;

import com.github.barrettotte.querysandbox.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * Get list of all orders
     * @return List of orders
     */
    List<Order> getAll();

    /**
     * Get order by id
     * @param id order ID
     * @return order
     */
    Optional<Order> getById(String id);

    /**
     * Create new order
     * @param order order to create
     * @return created order
     */
    Order create(Order order);

    /**
     * Update an existing order
     * @param original existing order
     * @param toUpdate updated order
     * @return updated order
     */
    Order update(Order original, Order toUpdate);

    /**
     * Delete an order by id
     * @param id order ID
     */
    void deleteById(String id);
}
