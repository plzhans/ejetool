package com.ejetool.account.service.dto.auth;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPublicKeyListResult {

    @Getter
    private final String issuer;

    @Getter
    private final List<PublicKeyDto> keys;
}
