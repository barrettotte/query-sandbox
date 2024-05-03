package com.github.barrettotte.querysandbox.api.controller;

import com.github.barrettotte.querysandbox.model.Order;
import com.github.barrettotte.querysandbox.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String home() {
        return "Order API";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return new ResponseEntity<Order>(new Order(), HttpStatusCode.valueOf(200));
    }

    // TODO: get all
    // TODO: create
    // TODO: update
    // TODO: delete
}
