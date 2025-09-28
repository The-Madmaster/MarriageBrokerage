package com.mahi.marriagebrokerage.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MfaConfig {
    
    @Bean
    public GoogleAuthenticator googleAuthenticator() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(30000) // 30 seconds
                .setWindowSize(3) // Allow 3 time windows (1.5 minutes tolerance)
                .setCodeDigits(6) // 6 digit codes
                .build();
        
        return new GoogleAuthenticator(config);
    }
}