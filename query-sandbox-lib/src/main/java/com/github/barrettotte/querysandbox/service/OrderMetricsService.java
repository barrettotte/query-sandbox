package com.github.barrettotte.querysandbox.service;

import com.github.barrettotte.querysandbox.metrics.*;

import java.util.List;
import java.util.Map;

public interface OrderMetricsService {

    Map<String, Long> distribution(DistributionSearch search);

    List<TrendBucket> trends(TrendsSearch search);

    DeltaResult delta(DateRangeSearch search);

    void collect();
}
