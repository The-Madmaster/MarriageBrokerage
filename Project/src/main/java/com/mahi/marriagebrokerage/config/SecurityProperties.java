package com.mahi.marriagebrokerage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {
    private Auth auth = new Auth();
    private RateLimit rateLimit = new RateLimit();

    @Data
    public static class Auth {
        private int maxFailedAttempts;
        private int lockoutMinutes;
    }

    @Data
    public static class RateLimit {
        private int windowSeconds;
        private int maxAttempts;
    }
}
