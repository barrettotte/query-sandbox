package com.github.barrettotte.querysandbox.metrics;

import com.github.barrettotte.querysandbox.model.OrderType;

import java.util.Objects;

public abstract class BaseMetricSearch {

    private Boolean includeHidden = false;
    private OrderType orderType = OrderType.BASIC;

    public Boolean getIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(Boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMetricSearch that = (BaseMetricSearch) o;
        return Objects.equals(includeHidden, that.includeHidden) &&
                orderType == that.orderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(includeHidden, orderType);
    }

    @Override
    public String toString() {
        return "BaseMetricSearch{" +
                "includeHidden=" + includeHidden +
                ", orderType=" + orderType +
                '}';
    }
}
