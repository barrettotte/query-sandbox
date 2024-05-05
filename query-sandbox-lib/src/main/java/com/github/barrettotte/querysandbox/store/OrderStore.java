package com.github.barrettotte.querysandbox.store;

import com.github.barrettotte.querysandbox.model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderStore {

    /**
     * Get all orders
     * @return all orders
     */
    List<Order> getAll();

    /**
     * Get an order by ID
     * @param id ID of order
     * @return order with given ID
     */
    Optional<Order> getById(String id);

    /**
     * Create a new order
     * @param order order to create
     * @return created order
     */
    Order create(Order order) throws SQLException;

    /**
     * Update an order
     * @param original original order
     * @param updated updated order
     * @return updated order
     */
    Order update(Order original, Order updated) throws SQLException;

    /**
     * Delete order by ID
     * @param id order to delete
     */
    void deleteById(String id);

}
