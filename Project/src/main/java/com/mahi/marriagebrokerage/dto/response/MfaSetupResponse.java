package com.mahi.marriagebrokerage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaSetupResponse {
    private String username;
    private String qrCodeUri;
    private String manualSetupKey;
}
