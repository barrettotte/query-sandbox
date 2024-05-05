package com.github.barrettotte.querysandbox.store.impl;

import com.github.barrettotte.querysandbox.model.Order;
import com.github.barrettotte.querysandbox.model.OrderBuilder;
import com.github.barrettotte.querysandbox.model.OrderRowMapper;
import com.github.barrettotte.querysandbox.store.OrderStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderStoreImpl implements OrderStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStoreImpl.class);

    @Autowired
    NamedParameterJdbcTemplate jdbcNamed;

    @Autowired
    JdbcTemplate jdbc;

    private final OrderRowMapper rowMapper = new OrderRowMapper();

    @Override
    public List<Order> getAll() {
        String sql = "SELECT * FROM orders";
        LOGGER.debug("Fetching orders: {}", sql);

        return jdbcNamed.query(sql, rowMapper);
    }

    @Override
    public Optional<Order> getById(String id) {
        String sql = "SELECT * FROM orders WHERE id=:id";
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id, Types.VARCHAR);
        LOGGER.debug("Fetching order by ID: {}\n{}", sql, params);

        return Optional.ofNullable(jdbcNamed.queryForObject(sql, params, rowMapper));
    }

    @Override
    public Order create(Order order) throws SQLException {
        // set generated values that user should not set
        order.setId(UUID.randomUUID().toString());
        order.setCreated(Instant.now());
        order.setUpdated(order.getCreated());

        String sql = "INSERT INTO orders (id, created, updated, description, status, category, priority, type, hidden, assignee_ids)\n" +
                "VALUES (:id, :created, :updated, :description, :status, :category, :priority, :type, :hidden, :assignee_ids)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", order.getId(), Types.VARCHAR)
                .addValue("created", Timestamp.from(order.getCreated()), Types.TIMESTAMP)
                .addValue("updated", Timestamp.from(order.getUpdated()), Types.TIMESTAMP)
                .addValue("description", order.getDescription(), Types.VARCHAR)
                .addValue("status", order.getStatus().name(), Types.VARCHAR)
                .addValue("category", order.getCategory().name(), Types.VARCHAR)
                .addValue("priority", order.getPriority().name(), Types.VARCHAR)
                .addValue("type", order.getType().name(), Types.VARCHAR)
                .addValue("hidden", order.getHidden(), Types.BOOLEAN)
                .addValue("assignee_ids", assigneeIdsToArray(order.getAssigneeIds()), Types.ARRAY);

        LOGGER.debug("Creating order: {}\n{}", sql, params);

        jdbcNamed.update(sql, params);
        return jdbc.queryForObject("SELECT * FROM orders WHERE id=?", rowMapper, order.getId());
    }

    @Override
    public Order update(Order original, Order toUpdate) throws SQLException {
        Order update = mergeUpdatesWithOriginal(original, toUpdate);
        update.setId(original.getId());
        update.setCreated(original.getCreated());
        update.setUpdated(Instant.now());

        String sql = "UPDATE orders SET updated=:updated, description=:description, status=:status, category=:category,\n" +
                "  priority=:priority, type=:type, hidden=:hidden, assignee_ids=:assignee_ids\n" +
                "WHERE id=:id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", update.getId(), Types.VARCHAR)
                .addValue("updated", Timestamp.from(update.getUpdated()), Types.TIMESTAMP)
                .addValue("description", update.getDescription(), Types.VARCHAR)
                .addValue("status", update.getStatus().name(), Types.VARCHAR)
                .addValue("category", update.getCategory().name(), Types.VARCHAR)
                .addValue("priority", update.getPriority().name(), Types.VARCHAR)
                .addValue("type", update.getType().name(), Types.VARCHAR)
                .addValue("hidden", update.getHidden(), Types.BOOLEAN)
                .addValue("assignee_ids", assigneeIdsToArray(update.getAssigneeIds()), Types.ARRAY);

        LOGGER.debug("Updating order: {}\n{}", sql, params);

        jdbcNamed.update(sql, params);
        return jdbc.queryForObject("SELECT * FROM orders WHERE id=?", rowMapper, update.getId());
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM orders WHERE id=:id";
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id, Types.VARCHAR);
        LOGGER.debug("Deleting order: {}\n{}", sql, params);

        jdbcNamed.update(sql, params);
    }

    /**
     * Merge updates with original order so the user does not have to update all fields in requests.
     * @param original original order
     * @param update updates to merge with original order
     * @return updated order
     */
    private Order mergeUpdatesWithOriginal(Order original, Order update) {
        return OrderBuilder.create()
                .withDescription(Optional.ofNullable(update.getDescription()).orElse(original.getDescription()))
                .withStatus(Optional.ofNullable(update.getStatus()).orElse(original.getStatus()))
                .withCategory(Optional.ofNullable(update.getCategory()).orElse(original.getCategory()))
                .withPriority(Optional.ofNullable(update.getPriority()).orElse(original.getPriority()))
                .withType(Optional.ofNullable(update.getType()).orElse(original.getType()))
                .withHidden(Optional.ofNullable(update.getHidden()).orElse(original.getHidden()))
                .withAssigneeIds(Optional.ofNullable(update.getAssigneeIds()).orElse(original.getAssigneeIds()))
                .build();
    }

    /**
     * Converts assignee ID list to SQL array for INSERT/UPDATE
     * @param assigneeIds List of assignee IDs
     * @return SQL array of assignee IDs
     * @throws SQLException
     */
    private Array assigneeIdsToArray(List<String> assigneeIds) throws SQLException {
        return Objects.requireNonNull(jdbc.getDataSource()).getConnection().createArrayOf("VARCHAR", assigneeIds.toArray());
    }
}
