package com.mahi.marriagebrokerage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mfa")
@Data
public class MfaProperties {
    private boolean enabled;
    private String issuer;
    private Email email = new Email();

    @Data
    public static class Email {
        private int codeExpirationSeconds;
        private int maxAttempts;
    }
}
