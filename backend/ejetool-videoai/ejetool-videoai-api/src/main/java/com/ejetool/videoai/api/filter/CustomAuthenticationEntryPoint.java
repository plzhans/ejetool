package com.ejetool.videoai.api.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.ejetool.videoai.api.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException 
    {
        if(log.isDebugEnabled()){
            log.debug("{}{}: AuthenticationException. Message: {}.", request.getContextPath(), request.getServletPath(), authException.getMessage());
            log.debug("{}{}: AuthenticationException. Cause: {}.", request.getContextPath(), request.getServletPath(), authException.getCause());
        }
        log.info("{}{}: Unauthorized. remote={}", request.getContextPath(), request.getServletPath(), request.getRemoteAddr());

        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED);
        String result = mapper.writeValueAsString(error);
        response.setStatus(error.getCode());
        response.getWriter().write(result);
    }
}