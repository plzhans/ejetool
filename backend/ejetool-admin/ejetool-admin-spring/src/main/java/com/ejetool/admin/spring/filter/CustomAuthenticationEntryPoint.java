package com.ejetool.admin.spring.filter;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException 
    {
        if(log.isDebugEnabled()){
            log.debug("{}{}: AuthenticationException. Message: {}.", request.getContextPath(), request.getServletPath(), authException.getMessage());
            log.debug("{}{}: AuthenticationException. Cause: {}.", request.getContextPath(), request.getServletPath(), authException.getCause());
        }
        log.info("{}{}: Unauthorized. remote={}", request.getContextPath(), request.getServletPath(), request.getRemoteAddr());
    }
}