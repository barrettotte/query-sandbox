package com.github.barrettotte.querysandbox.api.controller;

import com.github.barrettotte.querysandbox.service.OrderMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/metrics")
public class OrderMetricsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMetricsController.class);

    @Autowired
    private OrderMetricsService orderMetricsService;

    @PostMapping(value = "distribution", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> distribution() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "distribution/status", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> distributionStatus() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "distribution/category", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> distributionCategory() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "distribution/priority", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> distributionPriority() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "trends", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> trends() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "delta", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> delta() {
        // TODO:
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
