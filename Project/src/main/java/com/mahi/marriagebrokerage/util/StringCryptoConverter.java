package com.mahi.marriagebrokerage.util;

import com.mahi.marriagebrokerage.service.CryptoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Converter
public class StringCryptoConverter implements AttributeConverter<String, String> {

    private static CryptoService cryptoService;

    @Autowired
    public void setCryptoService(CryptoService cryptoService) {
        StringCryptoConverter.cryptoService = cryptoService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return cryptoService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return cryptoService.decrypt(dbData);
    }
}
