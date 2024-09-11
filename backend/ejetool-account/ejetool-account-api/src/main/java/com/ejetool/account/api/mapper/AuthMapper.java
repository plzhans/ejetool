package com.ejetool.account.api.mapper;

import org.mapstruct.Mapper;

import com.ejetool.account.api.dto.auth.GetPublicKeysResponse;
import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    GetPublicKeysResponse to(GetPublicKeyListResult result);
}
