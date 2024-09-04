package com.ejetool.admin.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AdminServerProperties adminServer;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        loginSuccessHandler.setTargetUrlParameter("redirectTo");
        loginSuccessHandler.setDefaultTargetUrl(this.adminServer.path("/"));
        
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher(this.adminServer.path("/instances"), "POST"),
                    new AntPathRequestMatcher(this.adminServer.path("/instances/*"), "DELETE"),
                    new AntPathRequestMatcher(this.adminServer.path("/actuator")),
                    new AntPathRequestMatcher(this.adminServer.path("/actuator/**"))
                )
            )
            //.sessionManagement(x->x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //.exceptionHandling(e -> {
            //     // RestExceptionAdvice 때문에 실행이 안됨.
            //     e.authenticationEntryPoint(customAuthenticationEntryPoint);
            //     e.accessDeniedHandler(customAccessDeniedHandler);
            //  })
            .authorizeHttpRequests(
                registry->registry
                    //.requestMatchers(this.adminServer.path("/actuator"), this.adminServer.path("/actuator/**")).permitAll()
                    .requestMatchers(this.adminServer.path("/assets/**")).permitAll()
                    .requestMatchers(this.adminServer.path("/login")).permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage(this.adminServer.path("/login"))
                .successHandler(loginSuccessHandler)
            )
            .httpBasic(Customizer.withDefaults())
            .rememberMe(rememberMe -> rememberMe
                .key(UUID.randomUUID().toString())
                .tokenValiditySeconds(1209600)
            )
            .logout(logout -> logout.permitAll());
        return http.build();
    }

    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
