
package com.ejetool.account.service.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicKeyDto {

    @Getter
    private final String id;

    @Getter
    private final String content;

}