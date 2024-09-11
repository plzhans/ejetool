
package com.ejetool.account.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejetool.account.api.dto.auth.GetPublicKeysResponse;
import com.ejetool.account.api.mapper.AuthMapper;
import com.ejetool.account.service.application.interfaces.AuthService;
import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * 인증 API
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    @SuppressWarnings("squid:S1075")
    @UtilityClass
    public static class Const {
        public static final String PATH_BASE = "/auth";

        public static final String PATH_GET_KEYS_PUBLIC = PATH_BASE + "/public-keys";
    }

    private final AuthService service;
    private final AuthMapper mapper;

    /**
     * 공개키 목록
     * @param request 요청
     * @return 응답
     */
    @Operation(summary = "공개키 목록", description = "공개키 생성")
    @GetMapping(Const.PATH_GET_KEYS_PUBLIC)
    public GetPublicKeysResponse getPublickKeys() {
        GetPublicKeyListResult result = service.getPublicKeyList();
        return mapper.to(result);
    }

}
