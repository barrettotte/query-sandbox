package com.github.barrettotte.querysandbox.metrics;

import java.time.Instant;
import java.util.Objects;

public class DateSearch extends BaseMetricSearch {
    private Instant metricDate;

    public Instant getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(Instant metricDate) {
        this.metricDate = metricDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DateSearch that = (DateSearch) o;
        return Objects.equals(metricDate, that.metricDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metricDate);
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "DateSearch{" +
                "metricDate=" + metricDate +
                '}';
    }
}
