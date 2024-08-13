package com.ejetool.videoai.api.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.ejetool.videoai.api.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) 
        throws IOException, ServletException 
    {
        log.info("Forbidden");

        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN);
        String result = mapper.writeValueAsString(error);
        response.setStatus(error.getCode());
        response.getWriter().write(result);
    }

}