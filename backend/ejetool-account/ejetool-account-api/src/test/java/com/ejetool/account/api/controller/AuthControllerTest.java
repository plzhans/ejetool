package com.ejetool.account.api.controller;

import static org.mockito.BDDMockito.given;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ejetool.account.api.config.SecurityConfig;
import com.ejetool.account.api.filter.CustomAccessDeniedHandler;
import com.ejetool.account.api.filter.CustomAuthenticationEntryPoint;
import com.ejetool.account.api.mapper.AuthMapperImpl;
import com.ejetool.account.service.application.interfaces.AuthService;
import com.ejetool.account.service.config.JwtConfig;
import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;
import com.ejetool.account.service.dto.auth.PublicKeyDto;

@Import({
    JwtConfig.class, 
    CustomAuthenticationEntryPoint.class, 
    CustomAccessDeniedHandler.class, 
    SecurityConfig.class,
    AuthMapperImpl.class
}) 
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void getPublickKeys__ok() throws Exception {
        String issuer = "http://www.test.com";
        GetPublicKeyListResult result = GetPublicKeyListResult.builder()
            .issuer(issuer)
            .keys(Arrays.asList(PublicKeyDto.builder()
                .id("id")
                .content("key")
                .build()))
            .build();
        
        given(authService.getPublicKeyList()).willReturn(result);
        
        mockMvc.perform(MockMvcRequestBuilders.get(AuthController.Const.PATH_GET_KEYS_PUBLIC))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.issuer").value(issuer))
               .andExpect(MockMvcResultMatchers.jsonPath("$.public_keys[0].id").value("id"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.public_keys[0].key").value("key"));
    }
}
