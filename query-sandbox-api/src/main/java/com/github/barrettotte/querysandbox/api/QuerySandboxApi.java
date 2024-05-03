package com.github.barrettotte.querysandbox.api;

import com.github.barrettotte.querysandbox.service.MyService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.github.barrettotte.querysandbox")
@RestController
public class QuerySandboxApi {

    private final MyService myService;

    public QuerySandboxApi(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/")
    public String home() {
        return myService.message();
    }

    public static void main(String[] args) {
        SpringApplication.run(QuerySandboxApi.class, args);
    }
}
