package com.github.barrettotte.querysandbox.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoreResult<T> {
    private long total = 0;
    private List<T> results = new ArrayList<>();

    public StoreResult() {
        // nop
    }

    public StoreResult(long total, List<T> results) {
        this.total = total;
        this.results = results;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreResult<?> that = (StoreResult<?>) o;
        return total == that.total && Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, results);
    }

    @Override
    public String toString() {
        return "StoreResult{" +
                "total=" + total + "," +
                "results=" + results +
                "}";
    }
}
