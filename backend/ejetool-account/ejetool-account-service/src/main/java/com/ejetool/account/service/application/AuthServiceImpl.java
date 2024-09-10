package com.ejetool.account.service.application;


import com.ejetool.account.service.application.interfaces.AuthService;
import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;
import com.ejetool.account.service.dto.auth.PublicKeyDto;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.security.Key;
import java.util.Base64;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtKeyStoreGenerator jwtKeyStoreGenerator;

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
    public GetPublicKeyListResult getPublicKeys() {
        List<PublicKeyDto> publicKeys = this.jwtKeyStoreGenerator.getStore().entrySet().stream()
            .map(entry -> {
                Key key = entry.getValue();
                String publicKeyString = Base64.getEncoder().encodeToString(key.getEncoded());
                publicKeyString = prettyFormat(publicKeyString, 64, "\\n");
                return PublicKeyDto.builder()
                    .id(entry.getKey())
                    .algorithm(key.getAlgorithm())
                    .foramt(key.getFormat())
                    .publicKey(publicKeyString)
                    .build();
            })
            .collect(Collectors.toList());
        return GetPublicKeyListResult.builder()
            .publicKeys(publicKeys)
            .build();
    }
    
}
