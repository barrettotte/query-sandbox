package com.github.barrettotte.querysandbox.metrics;

import com.github.barrettotte.querysandbox.model.OrderType;

public abstract class BaseMetricSearch {

    private Boolean includeHidden = false;
    private OrderType orderType = OrderType.BASIC;
    // TODO: criteria

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
}
