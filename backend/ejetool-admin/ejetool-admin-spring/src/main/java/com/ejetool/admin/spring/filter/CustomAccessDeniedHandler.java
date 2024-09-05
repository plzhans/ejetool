package com.ejetool.admin.spring.filter;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) 
        throws IOException, ServletException 
    {
        if(log.isDebugEnabled()){
            log.debug("{}{}: AccessDeniedException. Message: {}.", request.getContextPath(), request.getServletPath(), accessDeniedException.getMessage());
            log.debug("{}{}: AccessDeniedException. Cause: {}.", request.getContextPath(), request.getServletPath(), accessDeniedException.getCause());
        }
        log.info("{}{}: Forbidden. remote={}", request.getContextPath(), request.getServletPath(), request.getRemoteAddr());
    }

}