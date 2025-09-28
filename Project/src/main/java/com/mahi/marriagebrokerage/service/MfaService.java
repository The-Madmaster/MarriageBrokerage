package com.mahi.marriagebrokerage.service;

import com.mahi.marriagebrokerage.config.MfaProperties;
import com.mahi.marriagebrokerage.entity.User;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MfaService {

    private final MfaProperties mfaProperties;

    public MfaService(MfaProperties mfaProperties) {
        this.mfaProperties = mfaProperties;
    }

    public String generateNewSecret() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    public String generateQrCodeImageUri(String secret) {
        QrData data = new QrData.Builder()
                .label("MFA")
                .secret(secret)
                .issuer(mfaProperties.getIssuer())
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try {
            imageData = generator.generate(data);
        } catch (Exception e) {
            log.error("Error while generating QR code", e);
        }

        return Utils.getDataUriForImage(imageData, generator.getImageMimeType());
    }

    public boolean isTotpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }

    public String generateEmailOtp(User user) {
        // In a real application, you would generate a random code,
        // store it with an expiration date, and send it via email.
        // For this example, we'll just return a fixed code.
        return "123456";
    }

    public boolean isEmailOtpValid(User user, String code) {
        // In a real application, you would check the stored code and its expiration.
        return "123456".equals(code);
    }
}
