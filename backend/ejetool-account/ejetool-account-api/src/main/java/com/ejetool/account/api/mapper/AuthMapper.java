package com.ejetool.account.api.mapper;

import org.mapstruct.Mapper;

import com.ejetool.account.api.dto.auth.GetKeysResponse;
import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    GetKeysResponse to(GetPublicKeyListResult result);
}
