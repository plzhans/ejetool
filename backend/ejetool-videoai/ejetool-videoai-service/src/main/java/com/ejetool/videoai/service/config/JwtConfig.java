package com.ejetool.videoai.service.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ejetool.jwt.generator.JwtKeyStoreValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${auth.security.secret}")
    private String authSecuritySecret;

    @Value("${auth.security.public_key_path}")
    private String authSecurityPublicKeyPath;

    @Bean
    JwtKeyStoreValidator jwtKeyStoreValidator(ResourceLoader resourceLoader) throws IOException{
        var resourcePublic = resourceLoader.getResource(this.authSecurityPublicKeyPath);
        var publicKeyFile = resourcePublic.getFile();

        JwtKeyStoreValidator validator = new JwtKeyStoreValidator();
        validator.addSecretKey(this.authSecuritySecret, "s");
        if(publicKeyFile.exists()){
            validator.addPublicKeyPath(publicKeyFile.getPath());
        } else {
            log.warn("Notfound key file. auth.security.public_key_path="+authSecurityPublicKeyPath);
        }
        return validator;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
