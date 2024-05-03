package com.github.barrettotte.querysandbox.api.controller;

import com.github.barrettotte.querysandbox.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        return new ResponseEntity<>(testService.message(), HttpStatusCode.valueOf(200));
    }
}
