package com.github.barrettotte.querysandbox.metrics;

import java.time.Instant;
import java.util.Objects;

public class DateRangeSearch extends BaseMetricSearch {
    private Instant startDate;
    private Instant endDate;

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DateRangeSearch that = (DateRangeSearch) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, endDate);
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "DateRangeSearch{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
