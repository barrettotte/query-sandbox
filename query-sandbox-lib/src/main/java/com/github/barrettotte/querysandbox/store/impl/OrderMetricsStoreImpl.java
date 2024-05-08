package com.github.barrettotte.querysandbox.store.impl;

import com.github.barrettotte.querysandbox.store.OrderMetricsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;

@Component
public class OrderMetricsStoreImpl implements OrderMetricsStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStoreImpl.class);

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    NamedParameterJdbcTemplate jdbcNamed;

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
        LOGGER.debug("Order metrics collect started");
        Timestamp currentDate = Timestamp.from(Instant.now());

        // add orders that have not been recorded yet
        String createdSql = "INSERT (order_id, state_start, state_end, status, category, priority, type, hidden, assignee_ids)\n" +
                "INTO order_metrics\n" +
                "SELECT o.id, ?, NULL, o.status, o.category, o.priority, o.type, o.hidden, o.assignee_ids\n" +
                "FROM orders AS o\n" +
                "WHERE NOT EXISTS (SELECT o.id FROM order_metrics AS m WHERE m.order_id=o.id)";
        
        LOGGER.debug("Inserting new order metrics for newly created orders {}\nusing parameters {}", createdSql, List.of(currentDate));
        int inserted = jdbc.update(createdSql, currentDate);
        LOGGER.debug("Inserted {} new order_metrics row(s) for newly created orders", inserted);

        // update order metrics that changed since last metric collection
        String changesSql = "WITH order_changes AS (\n" +
            "  SELECT o.id\n" +
            "  FROM order_metrics AS m\n" +
            "  JOIN orders AS o ON m.order_id=o.id\n" +
            "  WHERE m.state_end IS NULL AND (\n" +
            "    o.status <> m.status OR\n" +
            "    o.category <> m.category OR\n" +
            "    o.priority <> m.priority OR\n" +
            "    o.hidden <> m.hidden OR\n" +
            "    o.assignee_ids <> m.assignee_ids\n" +
            "  )\n" +
            ")\n" +
            "UPDATE order_metrics\n" +
            "SET state_end=?\n" +
            "FROM order_changes\n" +
            "WHERE order_changes.id=order_metrics.order_id\n" +
            "  AND order_metrics.state_end IS NULL";

        LOGGER.debug("Updating order metrics {}\nusing parameters {}", changesSql, List.of(currentDate));
        int updated = jdbc.update(changesSql, currentDate);
        LOGGER.debug("Updated {} order_metrics row(s)", updated);

        // add a new state metric for order that just had its state change recorded
        String nextMetricSql = "WITH new_metrics AS (\n" +
            "  SELECT :currentDate AS state_start, NULL AS state_end, o.id, o.status,\n" +
            "    o.category, o.priority, o.type, o.hidden, o.assignee_ids\n" +
            "  FROM orders AS o\n" +
            "  WHERE NOT EXISTS (\n" +
            "    SELECT m.order_id\n" +
            "    FROM order_metrics AS m\n" +
            "    WHERE m.order_id=o.id AND m.state_end IS NULL\n" +
            "  )\n" +
            ")\n" +
            "INSERT INTO order_metrics (order_id, state_start, state_end, status,\n" +
            "  category, priority, type, hidden, assignee_ids)\n" +
            "SELECT o.id, :currentDate as state_start, NULL AS state_end, o.status,\n" +
            "  o.category, o.priority, o.type, o.hidden, o.assignee_ids\n" +
            "FROM orders AS o\n" +
            "WHERE NOT EXISTS (\n" +
            "  SELECT m.order_id\n" +
            "  FROM order_metrics AS m\n" +
            "  WHERE m.order_id=o.id AND m.state_end IS NULL\n" +
            ")";

        SqlParameterSource params = new MapSqlParameterSource().addValue("currentDate", currentDate, Types.TIMESTAMP);

        LOGGER.debug("Inserted new order metrics {}\nusing parameters {}", nextMetricSql, List.of(currentDate));
        inserted = jdbcNamed.update(nextMetricSql, params);
        LOGGER.debug("Inserted {} new order_metrics row(s)", inserted);

        LOGGER.debug("Order metrics collect ended");
    }
}
