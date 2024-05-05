package com.github.barrettotte.querysandbox.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {

    public static OrderBuilder create() {
        return new OrderBuilder();
    }

    public static OrderBuilder create(Order order) {
        return create()
                .withId(order.getId())
                .withCreated(order.getCreated())
                .withUpdated(order.getUpdated())
                .withDescription(order.getDescription())
                .withStatus(order.getStatus())
                .withCategory(order.getCategory())
                .withPriority(order.getPriority())
                .withType(order.getType())
                .withHidden(order.getHidden())
                .withAssigneeIds(order.getAssigneeIds());
    }

    private String id;
    private Instant created;
    private Instant updated;
    private String description;
    private Status status;
    private Category category;
    private Priority priority;
    private OrderType type;
    private Boolean hidden;
    private List<String> assigneeIds;

    private OrderBuilder() {
        // nop
    }

    public Order build() {
        return new Order(
                id == null ? UUID.randomUUID().toString() : id,
                created == null ? Instant.now() : created,
                updated == null ? Instant.now() : updated,
                description,
                status,
                category,
                priority,
                type,
                hidden != null && hidden,
                assigneeIds == null ? new ArrayList<>() : assigneeIds
        );
    }

    public OrderBuilder withId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
        return this;
    }

    public OrderBuilder withCreated(Instant created) {
        if (created == null) {
            throw new IllegalArgumentException("Created cannot be null");
        }
        this.created = created;
        return this;
    }

    public OrderBuilder withUpdated(Instant updated) {
        if (updated == null) {
            throw new IllegalArgumentException("Updated cannot be null");
        }
        this.updated = updated;
        return this;
    }

    public OrderBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public OrderBuilder withStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
        return this;
    }

    public OrderBuilder withCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
        return this;
    }

    public OrderBuilder withPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
        return this;
    }

    public OrderBuilder withType(OrderType type) {
        if (type == null) {
            throw new IllegalArgumentException("OrderType cannot be null");
        }
        this.type = type;
        return this;
    }

    public OrderBuilder withHidden(Boolean hidden) {
        if (hidden == null) {
            throw new IllegalArgumentException("Hidden cannot be null");
        }
        this.hidden = hidden;
        return this;
    }

    public OrderBuilder withAssigneeIds(List<String> assigneeIds) {
        if (assigneeIds == null) {
            assigneeIds = new ArrayList<>();
        }
        this.assigneeIds = assigneeIds;
        return this;
    }
}
