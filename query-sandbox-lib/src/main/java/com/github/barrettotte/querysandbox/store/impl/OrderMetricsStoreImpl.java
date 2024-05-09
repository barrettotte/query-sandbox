package com.github.barrettotte.querysandbox.store.impl;

import com.github.barrettotte.querysandbox.metrics.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderMetricsStoreImpl implements OrderMetricsStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStoreImpl.class);

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    NamedParameterJdbcTemplate jdbcNamed;

    @Override
    public Map<String, Long> distribution(DistributionSearch search) {
        LOGGER.debug("Calculating distributions");

        String distType = search.getMetricType().name().toLowerCase();
        String sql = "WITH dist AS (\n" +
                "  SELECT o.id, o.description, m.state_start, m.state_end, m.status, m.category, m.priority, m.hidden, m.type, m.assignee_ids\n" +
                "  FROM orders AS o\n" +
                "  JOIN order_metrics AS m ON m.order_id=o.id\n" +
                "    AND m.state_start <= :dist_date AND (m.state_end IS NULL OR m.state_end > :dist_date)\n" +
                ")\n" +
                "SELECT COUNT(*) AS count, " + distType + " FROM dist GROUP BY " + distType;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("dist_date", Timestamp.from(search.getMetricDate()), Types.TIMESTAMP);

        LOGGER.debug("Fetching distribution {}\nusing parameters {}", sql, params);

        Map<String, Long> dist = new HashMap<>();

        for (Map<String, Object> row: jdbcNamed.queryForList(sql, params)) {
            dist.put((String) row.get(distType), (Long) row.get("count"));
        }
        return dist;
    }

    @Override
    public List<TrendBucket> trends(TrendsSearch search) {
        LOGGER.debug("Calculating trends");

        String trendType = search.getMetricType().name().toLowerCase();
        String bucketIntervalSql = "INTERVAL '" + switch (search.getBucketSize()) {
            case HOUR -> "1 HOUR";
            case DAY -> "1 DAY";
            case MONTH -> "1 MONTH";
            case YEAR -> "1 YEAR";
        } + "'";

        String sql = "WITH buckets AS (\n" +
                "  SELECT b.bucket_start, (b.bucket_start + " + bucketIntervalSql + ") AS bucket_end\n" +
                "  FROM (\n" +
                "    SELECT GENERATE_SERIES(:start_date, :end_date, " + bucketIntervalSql + ") AS bucket_start\n" +
                "  ) AS b\n" +
                "),\n" +
                "most_recent_in_bucket AS (\n" +
                "  SELECT m.order_id, b.bucket_start, m.state_start, m.state_end, m." + trendType + ",\n" +
                "    RANK() OVER (\n" +
                "      PARTITION BY m.order_id, b.bucket_start\n" +
                "      ORDER BY m.state_start DESC\n" +
                "    ) AS rank\n" +
                "  FROM order_metrics AS m\n" +
                "  JOIN buckets AS b ON m.state_start < b.bucket_end\n" +
                "    AND (m.state_end IS NULL OR m.state_end >= b.bucket_start)\n" +
                ")\n" +
                "SELECT COUNT(*) AS bucket_count, bucket_start, " + trendType + "\n" +
                "FROM most_recent_in_bucket\n" +
                "WHERE rank=1\n" +
                "GROUP BY bucket_start, " + trendType + "\n" +
                "ORDER BY bucket_start, " + trendType;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_date", Timestamp.from(search.getStartDate()), Types.TIMESTAMP)
                .addValue("end_date", Timestamp.from(search.getEndDate()), Types.TIMESTAMP);

        LOGGER.debug("Fetching trends {}\nusing parameters {}", sql, params);

        List<TrendBucket> trendBuckets = new ArrayList<>();
        for (Map<String, Object> row : jdbcNamed.queryForList(sql, params)) {
            trendBuckets.add(new TrendBucket(
                    (String) row.get(trendType),
                    ((Timestamp) row.get("bucket_start")).toInstant(),
                    (Long) row.get("bucket_count")
            ));
        }
        return trendBuckets;
    }

    @Override
    public DeltaResult delta(DateRangeSearch search) {
        LOGGER.debug("Calculating deltas");

        String sql = "WITH metrics AS (\n" +
                "  SELECT order_id, state_start, state_end, status, category, priority,\n" +
                "    ROW_NUMBER() OVER (\n" +
                "      PARTITION BY order_id\n" +
                "      ORDER BY state_start\n" +
                "    ) AS rn\n" +
                "  FROM order_metrics\n" +
                "  WHERE state_start <= :end_date\n" +
                "),\n" +
                "deltas AS (\n" +
                "  SELECT a.order_id, a.state_start AS start_time, b.state_start AS end_time,\n" +
                "    ((a.status <> b.status) OR (a.category <> b.category) OR (a.priority <> b.priority)) AS is_delta\n" +
                "  FROM metrics AS a\n" +
                "  JOIN metrics AS b ON a.order_id=b.order_id AND a.rn = b.rn-1\n" +
                "),\n" +
                "latest_deltas AS (\n" +
                "  SELECT order_id, MAX(end_time) AS last_state_start\n" +
                "  FROM deltas\n" +
                "  WHERE is_delta=TRUE\n" +
                "  GROUP BY order_id\n" +
                "),\n" +
                "order_age AS (\n" +
                "  SELECT o.id AS order_id, EXTRACT(EPOCH FROM (:end_date - o.created) / (60 * 60 * 24)) AS age, o.created\n" +
                "  FROM orders AS o\n" +
                "  JOIN metrics AS m ON o.id=m.order_id AND m.rn=1\n" +
                "),\n" +
                "data_combined AS (\n" +
                "  SELECT o.id AS order_id, oa.age, d.is_delta,\n" +
                "    EXTRACT(EPOCH FROM (:end_date - COALESCE(ld.last_state_start, o.created)) / (60 * 60 * 24)) AS inactivity\n" +
                "  FROM orders AS o\n" +
                "  JOIN order_age AS oa ON o.id=oa.order_id\n" +
                "  LEFT JOIN latest_deltas AS ld ON o.id=ld.order_id\n" +
                "  LEFT JOIN deltas AS d ON o.id=d.order_id\n" +
                ")\n" +
                "SELECT ROUND(MIN(age), 4) AS min_age,\n" +
                "  ROUND(MAX(age), 4) AS max_age,\n" +
                "  ROUND(AVG(age), 4) AS avg_age,\n" +
                "  ROUND(MIN(inactivity), 4) AS min_inactivity,\n" +
                "  ROUND(MAX(inactivity), 4) AS max_inactivity,\n" +
                "  ROUND(AVG(inactivity), 4) AS avg_inactivity,\n" +
                "  COUNT(is_delta) AS delta_count,\n" +
                "  (SELECT COUNT(DISTINCT order_id) FROM metrics) AS total_count\n" +
                "FROM data_combined";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("end_date", Timestamp.from(search.getEndDate()), Types.TIMESTAMP);

        LOGGER.debug("Fetching deltas {}\nusing parameters {}", sql, params);

        DeltaResult deltaResult = new DeltaResult();
        for (Map<String, Object> row : jdbcNamed.queryForList(sql, params)) {
            deltaResult.setMinAge((Float) row.get("min_age"));
            deltaResult.setMinAge((Float) row.get("max_age"));
            deltaResult.setAverageAge((Float) row.get("avg_age"));

            deltaResult.setMinInactivity((Float) row.get("min_inactivity"));
            deltaResult.setMaxInactivity((Float) row.get("max_inactivity"));
            deltaResult.setAverageInactivity((Float) row.get("avg_inactivity"));

            deltaResult.setDeltaCount((Long) row.get("delta_count"));
            deltaResult.setTotalCount((Long) row.get("total_count"));
        }
        return deltaResult;
    }

    @Override
    public void collect() {
        LOGGER.debug("Order metrics collect started");
        Timestamp currentDate = Timestamp.from(Instant.now());

        // add orders that have not been recorded yet
        String createdSql = "INSERT INTO order_metrics (order_id, state_start, state_end, status, category, priority, type, hidden, assignee_ids)\n" +
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
