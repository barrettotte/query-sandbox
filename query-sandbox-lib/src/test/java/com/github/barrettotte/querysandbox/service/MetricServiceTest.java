package com.github.barrettotte.querysandbox.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest("service.message=Hello")
public class MetricServiceTest {

    @Autowired
    private MetricService metricService;

    @Test
    public void contextLoads() {
        assertThat(metricService.message()).isNotNull();
    }

    @SpringBootApplication
    static class TestConfiguration {
        // nop
    }

}