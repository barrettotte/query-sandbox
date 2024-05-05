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
        Order order = new Order();

        order.setId(rs.getString("id"));
        order.setCreated(rs.getObject("created", LocalDateTime.class).toInstant(ZoneOffset.UTC));
        order.setUpdated(rs.getObject("updated", LocalDateTime.class).toInstant(ZoneOffset.UTC));
        order.setDescription(rs.getString("description"));
        order.setStatus(Status.valueOf(rs.getString("status")));
        order.setCategory(Category.valueOf(rs.getString("category")));
        order.setPriority(Priority.valueOf(rs.getString("priority")));
        order.setType(OrderType.valueOf(rs.getString("type")));
        order.setHidden(rs.getBoolean("hidden"));
        order.setAssigneeIds(Arrays.asList((String[]) rs.getArray("assignee_ids").getArray()));

        return order;
    }
}
