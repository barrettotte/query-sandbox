package com.github.barrettotte.querysandbox.metrics;

import java.util.Objects;

public class DistributionSearch extends DateSearch {
    private MetricType metricType;

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DistributionSearch that = (DistributionSearch) o;
        return metricType == that.metricType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metricType);
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "DistributionSearch{" +
                "metricType=" + metricType +
                '}';
    }
}
