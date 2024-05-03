package com.github.barrettotte.querysandbox.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service.test")
public class TestServiceProperties {

    /**
     * A test message not useful for anything.
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
