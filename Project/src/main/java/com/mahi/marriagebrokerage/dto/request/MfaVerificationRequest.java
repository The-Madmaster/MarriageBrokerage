package com.mahi.marriagebrokerage.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MfaVerificationRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String code;
}
