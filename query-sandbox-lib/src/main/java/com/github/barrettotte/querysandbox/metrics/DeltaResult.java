package com.github.barrettotte.querysandbox.metrics;

import java.util.Objects;

public class DeltaResult {
    private Long totalCount;
    private Long deltaCount;

    private Float minAge;
    private Float averageAge;
    private Float maxAge;

    private Float minInactivity;
    private Float averageInactivity;
    private Float maxInactivity;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long deltaCount() {
        return deltaCount;
    }

    public void setDeltaCount(Long deltaCount) {
        this.deltaCount = deltaCount;
    }

    public Long getDeltaCount() {
        return deltaCount;
    }

    public Float getMinAge() {
        return minAge;
    }

    public void setMinAge(Float minAge) {
        this.minAge = minAge;
    }

    public Float getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(Float averageAge) {
        this.averageAge = averageAge;
    }

    public Float getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Float maxAge) {
        this.maxAge = maxAge;
    }

    public Float getMinInactivity() {
        return minInactivity;
    }

    public void setMinInactivity(Float minInactivity) {
        this.minInactivity = minInactivity;
    }

    public Float getAverageInactivity() {
        return averageInactivity;
    }

    public void setAverageInactivity(Float averageInactivity) {
        this.averageInactivity = averageInactivity;
    }

    public Float getMaxInactivity() {
        return maxInactivity;
    }

    public void setMaxInactivity(Float maxInactivity) {
        this.maxInactivity = maxInactivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeltaResult that = (DeltaResult) o;
        return Objects.equals(totalCount, that.totalCount) &&
                Objects.equals(deltaCount, that.deltaCount) &&
                Objects.equals(minAge, that.minAge) &&
                Objects.equals(averageAge, that.averageAge) &&
                Objects.equals(maxAge, that.maxAge) &&
                Objects.equals(minInactivity, that.minInactivity) &&
                Objects.equals(averageInactivity, that.averageInactivity) &&
                Objects.equals(maxInactivity, that.maxInactivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCount, deltaCount, minAge, averageAge, maxAge, minInactivity, averageInactivity, maxInactivity);
    }

    @Override
    public String toString() {
        return "DeltaResult{" +
                "totalCount=" + totalCount +
                ", deltaCount=" + deltaCount +
                ", minAge=" + minAge +
                ", averageAge=" + averageAge +
                ", maxAge=" + maxAge +
                ", minInactivity=" + minInactivity +
                ", averageInactivity=" + averageInactivity +
                ", maxInactivity=" + maxInactivity +
                '}';
    }
}
