package com.github.barrettotte.querysandbox.metrics;

import java.time.Instant;
import java.util.Objects;

public class TrendBucket {
    private String key;
    private Instant bucketDate;
    private Long count;

    public TrendBucket() {
        // nop
    }

    public TrendBucket(String key, Instant bucketDate, Long count) {
        this.key = key;
        this.bucketDate = bucketDate;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Instant getBucketDate() {
        return bucketDate;
    }

    public void setBucketDate(Instant bucketDate) {
        this.bucketDate = bucketDate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrendBucket that = (TrendBucket) o;
        return Objects.equals(key, that.key) && Objects.equals(bucketDate, that.bucketDate) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, bucketDate, count);
    }

    @Override
    public String toString() {
        return "TrendBucket{" +
                "key='" + key + '\'' +
                ", bucketDate=" + bucketDate +
                ", count=" + count +
                '}';
    }
}
