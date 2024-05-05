package com.github.barrettotte.querysandbox.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "created", nullable = false)
    private Instant created;

    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "type", nullable = false)
    private OrderType type;

    @Column(name = "hidden", nullable = false)
    private Boolean hidden;

    @Column(name = "assignee_ids")
    private List<String> assigneeIds = new ArrayList<>();

    public Order() {
        // nop
    }

    public Order(String id, Instant created, Instant updated, String description, Status status, Category category,
                 Priority priority, OrderType type, Boolean hidden, List<String> assigneeIds) {
        this.id = id;
        this.created = created;
        this.updated = updated;
        this.description = description;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.type = type;
        this.hidden = hidden;
        this.assigneeIds = assigneeIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public List<String> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(List<String> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Order other)) {
            return false;
        }
        return (Objects.equals(id, other.id) &&
                Objects.equals(created, other.created) &&
                Objects.equals(updated, other.updated) &&
                Objects.equals(description, other.description) &&
                Objects.equals(status, other.status) &&
                Objects.equals(category, other.category) &&
                Objects.equals(priority, other.priority) &&
                Objects.equals(type, other.type) &&
                Objects.equals(hidden, other.hidden) &&
                Objects.equals(assigneeIds, other.assigneeIds)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, updated, description, status, category, priority, type, hidden, assigneeIds);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + "'," +
                "created=" + created + "," +
                "updated=" + updated + "," +
                "description='" + description + "'," +
                "status=" + status + "," +
                "category=" + category + "," +
                "priority=" + priority + "," +
                "orderType=" + type + "," +
                "hidden=" + hidden + "," +
                "assigneeIds=" + assigneeIds +
                "}";
    }

}
