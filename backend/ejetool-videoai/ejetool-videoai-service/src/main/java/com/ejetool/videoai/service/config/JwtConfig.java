package com.ejetool.videoai.service.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ejetool.jwt.generator.JwtKeyStoreValidator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtConfig.AuthSecuritySettings.class)
public class JwtConfig {
    
    @ConfigurationProperties("auth.security")
    public static class AuthSecuritySettings {
        @Getter @Setter
        private String[] allowedIssuers;
    
        @Value("${auth.security.secret}")
        @Getter @Setter
        private String secret;
    
        @Value("${auth.security.public_key_path}")
        @Getter @Setter
        private String publicKeyPath;
    }
    private final AuthSecuritySettings settings;

    @Bean
    JwtKeyStoreValidator jwtKeyStoreValidator(ResourceLoader resourceLoader) throws IOException{
        var resourcePublic = resourceLoader.getResource(this.settings.getPublicKeyPath());
        var publicKeyFile = resourcePublic.getFile();

        JwtKeyStoreValidator validator = JwtKeyStoreValidator.build(
            o->o.addIssuer(this.settings.getAllowedIssuers())
        );
        validator.addSecretKey(this.settings.getSecret(), "s");
        if(publicKeyFile.exists()){
            validator.addPublicKeyPath(publicKeyFile.getPath());
        } else {
            log.warn("Notfound key file. auth.security.public_key_path="+this.settings.getPublicKeyPath());
        }
        return validator;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
