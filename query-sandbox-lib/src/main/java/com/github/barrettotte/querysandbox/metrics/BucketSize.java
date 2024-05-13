package com.github.barrettotte.querysandbox.metrics;

import java.time.temporal.ChronoUnit;

public enum BucketSize {
    YEAR(ChronoUnit.DAYS),
    MONTH(ChronoUnit.DAYS),
    DAY(ChronoUnit.DAYS),
    HOUR(ChronoUnit.HOURS);

    private final ChronoUnit truncateTo;

    BucketSize(ChronoUnit truncateTo) {
        this.truncateTo = truncateTo;
    }

    public ChronoUnit getTruncateTo() {
        return truncateTo;
    }
}
