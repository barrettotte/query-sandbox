package com.github.barrettotte.querysandbox.metrics;

import java.util.Objects;

public class TrendsSearch extends DateRangeSearch {
    private MetricType metricType;
    private BucketSize bucketSize;

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public BucketSize getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(BucketSize bucketSize) {
        this.bucketSize = bucketSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrendsSearch that = (TrendsSearch) o;
        return metricType == that.metricType && bucketSize == that.bucketSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metricType, bucketSize);
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "TrendsSearch{" +
                "metricType=" + metricType +
                ", bucketSize=" + bucketSize +
                '}';
    }
}
