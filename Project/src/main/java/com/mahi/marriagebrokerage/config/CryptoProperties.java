package com.mahi.marriagebrokerage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "crypto")
@Data
public class CryptoProperties {
    private String secret;
}
