package com.github.barrettotte.querysandbox.service.impl;

import com.github.barrettotte.querysandbox.service.TestService;
import com.github.barrettotte.querysandbox.service.properties.TestServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(TestServiceProperties.class)
public class TestServiceImpl implements TestService {

    @Autowired
    private TestServiceProperties testServiceProperties;

//    public TestServiceImpl(TestServiceProperties testServiceProperties) {
//        this.testServiceProperties = testServiceProperties;
//    }

    public String message() {
        return this.testServiceProperties.getMessage();
    }
}
