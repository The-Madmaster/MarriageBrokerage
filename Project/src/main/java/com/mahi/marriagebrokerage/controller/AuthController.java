package com.mahi.marriagebrokerage.controller;

import com.mahi.marriagebrokerage.dto.request.LoginRequest;
import com.mahi.marriagebrokerage.dto.request.RefreshTokenRequest;
import com.mahi.marriagebrokerage.dto.request.UserRegistrationRequest;
import com.mahi.marriagebrokerage.dto.response.JwtResponse;
import com.mahi.marriagebrokerage.entity.RefreshToken;
import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import com.mahi.marriagebrokerage.security.JwtUtils;
import com.mahi.marriagebrokerage.service.AccountLockoutService;
import com.mahi.marriagebrokerage.service.MfaService;
import com.mahi.marriagebrokerage.service.RateLimitService;
import com.mahi.marriagebrokerage.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private MfaService mfaService;
    @Autowired
    private AccountLockoutService accountLockoutService;
    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Check if user exists first
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));
            
            // Check if account is locked
            if (accountLockoutService.isAccountLocked(user)) {
                return ResponseEntity.badRequest().body("Account is locked due to too many failed attempts. Please try again later.");
            }
            
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            } catch (Exception e) {
                // Record failed attempt
                accountLockoutService.recordFailedAttempt(user);
                throw new RuntimeException("Invalid credentials");
            }
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            user = (User) authentication.getPrincipal();
            
            // Check if MFA is required
            if (mfaService.requiresMfa(user)) {
                // For now, return a temporary token that requires MFA verification
                // In a full implementation, you'd store this state and require MFA completion
                return ResponseEntity.ok().body(new MfaRequiredResponse(user.getId(), "MFA required"));
            }
            
            // Record successful login
            accountLockoutService.recordSuccessfulLogin(user);
            rateLimitService.resetLoginAttempts(loginRequest.getUsername());
            
            String jwt = jwtUtils.generateJwtToken(authentication);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            
            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), user.getId(), 
                    user.getUsername(), user.getEmail(), user.getRole().name()));
                    
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    if (!refreshTokenService.verifyExpiration(refreshToken)) {
                        return ResponseEntity.badRequest().body("Refresh token is expired or revoked!");
                    }
                    
                    User user = refreshToken.getUser();
                    String newAccessToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                    
                    return ResponseEntity.ok(new JwtResponse(newAccessToken, requestRefreshToken, 
                            user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        refreshTokenService.findByToken(requestRefreshToken)
                .ifPresent(refreshTokenService::revokeToken);
        
        return ResponseEntity.ok("Logged out successfully!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setFullName(signUpRequest.getFullName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole(signUpRequest.getRole());
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }
    
    @Data
    public static class MfaRequiredResponse {
        private Long userId;
        private String message;
        private String status = "MFA_REQUIRED";
        
        public MfaRequiredResponse(Long userId, String message) {
            this.userId = userId;
            this.message = message;
        }
    }
}
