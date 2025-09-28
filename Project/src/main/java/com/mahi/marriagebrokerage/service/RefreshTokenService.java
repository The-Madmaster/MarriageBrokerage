package com.mahi.marriagebrokerage.service;

import com.mahi.marriagebrokerage.entity.RefreshToken;
import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    
    @Value("${jwt.refresh.expiration:604800000}") // 7 days
    private long refreshTokenExpirationMs;
    
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // Revoke existing tokens for the user
        refreshTokenRepository.revokeAllUserTokens(user);
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000));
        
        return refreshTokenRepository.save(refreshToken);
    }
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    public boolean verifyExpiration(RefreshToken token) {
        if (token.isExpired() || token.isRevoked()) {
            log.info("Refresh token {} is expired or revoked", token.getToken());
            return false;
        }
        return true;
    }
    
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
    
    @Transactional
    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
    
    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}