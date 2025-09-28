package com.mahi.marriagebrokerage.service;

import com.mahi.marriagebrokerage.config.CryptoProperties;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    private final SecretKeySpec secretKey;
    private final Cipher cipher;

    public CryptoService(CryptoProperties cryptoProperties) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(cryptoProperties.getSecret());
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        this.cipher = Cipher.getInstance("AES");
    }

    public String encrypt(String data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
