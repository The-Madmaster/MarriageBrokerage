package com.mahi.marriagebrokerage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /** Raw secret string (BASE64 expected for current JwtUtils implementation). */
    private String secret;
    /** Expiration in milliseconds. */
    private long expiration;
}
