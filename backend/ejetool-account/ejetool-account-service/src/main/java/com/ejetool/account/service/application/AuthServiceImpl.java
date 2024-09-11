package com.ejetool.account.service.application;

import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ejetool.account.service.application.interfaces.AuthService;
import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;
import com.ejetool.account.service.dto.auth.PublicKeyDto;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;

@Service
public class AuthServiceImpl implements AuthService {
    
    private final String issuer;
    private final JwtKeyStoreGenerator jwtKeyStoreGenerator;

    public AuthServiceImpl(@Value("${auth.security.issuer}") String issuer, JwtKeyStoreGenerator jwtKeyStoreGenerator){
        this.issuer = issuer;
        this.jwtKeyStoreGenerator = jwtKeyStoreGenerator;
    }

    private String prettyFormat(String input, int lineLength, String newLine) {
        StringBuilder result = new StringBuilder();
        result.append("-----BEGIN PUBLIC KEY-----").append(newLine);
        int index = 0;
        while (index < input.length()) {
            result.append(input, index, Math.min(index + lineLength, input.length()));
            result.append(newLine);
            index += lineLength;
        }
        result.append("-----END PUBLIC KEY-----");
        return result.toString();
    }
    
    @Override
    public GetPublicKeyListResult getPublicKeyList() {
        List<PublicKeyDto> keys = this.jwtKeyStoreGenerator.getPublicKeyMap().entrySet().stream()
            .map(entry -> {
                PublicKey key = entry.getValue();
                String publicKeyString = Base64.getEncoder().encodeToString(key.getEncoded());
                publicKeyString = prettyFormat(publicKeyString, 64, "\\n");
                return PublicKeyDto.builder()
                    .id(entry.getKey())
                    .content(publicKeyString)
                    .build();
            })
            .collect(Collectors.toList());
        return GetPublicKeyListResult.builder()
            .issuer(this.issuer)
            .keys(keys)
            .build();
    }
    
}
