package com.ejetool.account.service.dto.auth;

import java.util.List;

import lombok.Builder;
import lombok.Getter;


public class GetPublicKeyListResult {
    @Getter
    private List<PublicKeyDto> publicKeys;

    @Builder
    public GetPublicKeyListResult(List<PublicKeyDto> publicKeys){
        this.publicKeys = publicKeys;
    }
}
