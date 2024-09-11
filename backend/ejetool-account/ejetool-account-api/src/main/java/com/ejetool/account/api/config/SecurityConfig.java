package com.ejetool.account.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.ejetool.jwt.generator.JwtKeyStoreValidator;
import com.ejetool.account.api.controller.AuthController;
import com.ejetool.account.api.filter.CustomAccessDeniedHandler;
import com.ejetool.account.api.filter.CustomAuthenticationEntryPoint;
import com.ejetool.account.api.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
//@EnableWebSecurity // 선언안해도 되게 바뀜
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig{

    //private final CorsFilter corsFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Value("${management.endpoints.web.base-path:/actuator}")
    private String managementBasePath;

    private static final String[] ALLOWED_URLS = {
        "/", "/error", "/signup", "/signin", 
        AuthController.Const.PATH_GET_KEYS_PUBLIC
    };
    
    private static final String[] SWAGGER_URLS = {"/docs","/docs/**","/docs/swagger-ui/**"};
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtKeyStoreValidator jwtKeyStoreValidator) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtKeyStoreValidator);
        http
            .csrf(x->x.disable())
            //.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> {
                // RestExceptionAdvice 때문에 실행이 안됨.
                e.authenticationEntryPoint(customAuthenticationEntryPoint);
                e.accessDeniedHandler(customAccessDeniedHandler);
             })
            .headers(x->x.frameOptions(o->o.sameOrigin()))
            .sessionManagement(x->x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry -> {
                registry.requestMatchers(ALLOWED_URLS).permitAll()
                    .requestMatchers(SWAGGER_URLS).permitAll();
                if(StringUtils.hasText(managementBasePath)){
                    registry.requestMatchers(new String[]{managementBasePath, managementBasePath+"/**"}).permitAll();
                }
                //registry.anyRequest().authenticated();
                registry.anyRequest().permitAll();
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // @Bean
    // WebSecurityCustomizer webSecurityCustomizer() {
    //     return (web) -> web.ignoring()
    //             .requestMatchers("/ignore1", "/ignore2");
    // }

}
