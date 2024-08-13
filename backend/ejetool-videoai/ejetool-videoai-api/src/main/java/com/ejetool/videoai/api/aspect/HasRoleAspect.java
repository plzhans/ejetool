package com.ejetool.videoai.api.aspect;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ejetool.videoai.api.annotation.HasRole;

@Aspect
@Component
public class HasRoleAspect {
    @Before("@annotation(hasRole)")
    public void checkPermission(JoinPoint joinPoint, HasRole hasRole) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            throw new AccessDeniedException("Access Denied");
        }

        var roles = Arrays.stream(hasRole.value());
        boolean match = roles
            .anyMatch(x->"*".equals(x));
        if(!match){
            match = authentication.getAuthorities()
                .stream()
                .map(x->x.getAuthority())
                .collect(Collectors.toList())
                .containsAll(roles.toList());
        }
        if(!match){
            throw new AccessDeniedException("Access Denied");
        }
    }
}
