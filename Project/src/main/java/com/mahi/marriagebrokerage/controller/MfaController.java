package com.mahi.marriagebrokerage.controller;

import com.mahi.marriagebrokerage.dto.request.MfaVerificationRequest;
import com.mahi.marriagebrokerage.dto.response.MfaSetupResponse;
import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.UserRepository;
import com.mahi.marriagebrokerage.service.MfaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mfa")
public class MfaController {

    private final MfaService mfaService;
    private final UserRepository userRepository;

    public MfaController(MfaService mfaService, UserRepository userRepository) {
        this.mfaService = mfaService;
        this.userRepository = userRepository;
    }

    @PostMapping("/setup")
    public ResponseEntity<?> setupMfa() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String secret = mfaService.generateNewSecret();
        user.setMfaSecret(secret);
        user.setMfaEnabled(false); // Keep it disabled until verified
        userRepository.save(user);
        String qrCodeUri = mfaService.generateQrCodeImageUri(secret);
        return ResponseEntity.ok(new MfaSetupResponse(user.getUsername(), qrCodeUri, secret));
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateMfa(@Valid @RequestBody MfaVerificationRequest verificationRequest) {
        User user = userRepository.findByUsername(verificationRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (mfaService.isTotpValid(user.getMfaSecret(), verificationRequest.getCode())) {
            user.setMfaEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("MFA has been activated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code.");
        }
    }
}
