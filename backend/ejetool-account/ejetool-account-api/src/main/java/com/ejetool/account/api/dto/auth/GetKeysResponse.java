package com.ejetool.account.api.dto.auth;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetKeysResponse {
    @Getter
    private final String issuer;

    @Getter
    private final List<PublicKeyData> keys;
}
