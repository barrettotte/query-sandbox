package com.github.barrettotte.querysandbox.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void contextLoads() {
        // nop
    }

    @SpringBootApplication
    static class TestConfiguration {
        // nop
    }

}
