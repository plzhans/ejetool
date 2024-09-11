package com.ejetool.account.api.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicKeyData {
    @Getter
    private final String id;

    @Getter
    private final String content;
}
