package com.github.barrettotte.querysandbox.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest("service.test.message=TEST!")
public class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void contextLoads() {
        assertThat(testService.message()).isNotNull();
    }

    @SpringBootApplication
    static class TestConfiguration {
        // nop
    }

}
