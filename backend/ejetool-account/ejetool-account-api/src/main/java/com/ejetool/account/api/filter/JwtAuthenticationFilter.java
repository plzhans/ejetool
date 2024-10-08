package com.ejetool.account.api.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ejetool.jwt.generator.JwtKeyStoreValidator;
import com.ejetool.account.api.dto.auth.AuthUser;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtKeyStoreValidator jwtKeyStoreValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseToken(request);
        if(StringUtils.hasText(token)){
            AuthUser user = this.verifyUser(token);
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }
        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(token -> token.length() > 7)
                .filter(token -> token.startsWith("Bearer ") || token.startsWith("bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    public AuthUser verifyUser(String token){
        Claims claims = this.jwtKeyStoreValidator.verify(token);
        AuthUser user = this.getAuthUser(claims);
        return user;
    }

    public AuthUser getAuthUser(Claims claims){
        @SuppressWarnings("unchecked")
        var authorities = ((List<String>)claims.get("roles", ArrayList.class)).stream()
            .map(x-> new SimpleGrantedAuthority(x))
            .collect(Collectors.toList());
        AuthUser user = new AuthUser(claims.getSubject(), "", authorities);
        return user;
    }
}