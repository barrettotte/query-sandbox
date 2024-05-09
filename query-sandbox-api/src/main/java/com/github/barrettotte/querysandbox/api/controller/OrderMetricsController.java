package com.github.barrettotte.querysandbox.api.controller;

import com.github.barrettotte.querysandbox.metrics.*;
import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order/metrics")
public class OrderMetricsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMetricsController.class);

    @Autowired
    private OrderMetricsService orderMetricsService;

    @PostMapping(value = "distribution", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Long>> distributionStatus(@RequestBody DistributionSearch search) {
        return new ResponseEntity<>(orderMetricsService.distribution(search), HttpStatus.OK);
    }

    @PostMapping(value = "trends", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<TrendBucket>> trends(@RequestBody TrendsSearch search) {
        return new ResponseEntity<>(orderMetricsService.trends(search), HttpStatus.OK);
    }

    @PostMapping(value = "delta", consumes = "application/json", produces = "application/json")
    public ResponseEntity<DeltaResult> delta(@RequestBody DateRangeSearch search) {
        return new ResponseEntity<>(orderMetricsService.delta(search), HttpStatus.OK);
    }

    @GetMapping(value = "collect")
    public ResponseEntity<Void> collect() {
        orderMetricsService.collect();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
