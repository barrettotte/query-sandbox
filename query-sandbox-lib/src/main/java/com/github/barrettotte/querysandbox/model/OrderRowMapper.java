package com.github.barrettotte.querysandbox.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrderBuilder.create()
                .withId(rs.getString("id"))
                .withCreated(rs.getObject("created", LocalDateTime.class).toInstant(ZoneOffset.UTC))
                .withUpdated(rs.getObject("updated", LocalDateTime.class).toInstant(ZoneOffset.UTC))
                .withDescription(rs.getString("description"))
                .withStatus(Status.valueOf(rs.getString("status")))
                .withCategory(Category.valueOf(rs.getString("category")))
                .withPriority(Priority.valueOf(rs.getString("priority")))
                .withType(OrderType.valueOf(rs.getString("type")))
                .withHidden(rs.getBoolean("hidden"))
                .withAssigneeIds(Arrays.asList((String[]) rs.getArray("assignee_ids").getArray()))
                .build();
    }
}
