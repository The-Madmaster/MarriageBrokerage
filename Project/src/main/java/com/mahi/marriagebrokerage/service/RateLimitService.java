package com.mahi.marriagebrokerage.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class RateLimitService {
    
    private final ConcurrentMap<String, Bucket> cache = new ConcurrentHashMap<>();
    
    public boolean isAllowed(String key, int maxAttempts, Duration duration) {
        Bucket bucket = cache.computeIfAbsent(key, k -> createNewBucket(maxAttempts, duration));
        return bucket.tryConsume(1);
    }
    
    public boolean isLoginAllowed(String identifier) {
        // Allow 5 login attempts per 15 minutes
        return isAllowed("login:" + identifier, 5, Duration.ofMinutes(15));
    }
    
    public boolean isApiCallAllowed(String identifier) {
        // Allow 100 API calls per minute
        return isAllowed("api:" + identifier, 100, Duration.ofMinutes(1));
    }
    
    public void resetBucket(String key) {
        cache.remove(key);
    }
    
    public void resetLoginAttempts(String identifier) {
        resetBucket("login:" + identifier);
    }
    
    private Bucket createNewBucket(int capacity, Duration duration) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, duration));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}