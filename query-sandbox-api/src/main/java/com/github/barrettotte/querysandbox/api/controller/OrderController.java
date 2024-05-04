package com.github.barrettotte.querysandbox.api.controller;

import com.github.barrettotte.querysandbox.model.Order;
import com.github.barrettotte.querysandbox.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<List<Order>> getAll() {
        // TODO: orderService.getAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Order> getById(@PathVariable(value = "id") String id) {
        // TODO: orderService.getById(id);
        // TODO: 404 if not found
        return new ResponseEntity<>(new Order(), HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Order> create(@RequestBody Order toCreate) {
        // TODO: orderService.create(toCreate);
        return new ResponseEntity<>(toCreate, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Order> update(@PathVariable(value = "id") String id, @RequestBody Order toUpdate) {
        // TODO: Optional<Order> original = orderService.getById(id);
        // TODO: 404 if not found
        // TODO: orderService.update(original, toUpdate);
        return new ResponseEntity<>(toUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") String id) {
        // TODO: 404 if not found
        // TODO: orderService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
