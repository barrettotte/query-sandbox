package com.github.barrettotte.querysandbox.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.barrettotte.querysandbox")
public class QuerySandboxApi {
    public static void main(String[] args) {
        SpringApplication.run(QuerySandboxApi.class, args);
    }
}
