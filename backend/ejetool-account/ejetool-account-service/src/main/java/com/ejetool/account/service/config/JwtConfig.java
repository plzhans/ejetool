package com.ejetool.account.service.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
import com.ejetool.jwt.generator.JwtKeyStoreValidator;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${auth.security.secret}")
    private String authSecuritySecret;

    @Value("${auth.security.private_key_path}")
    private String authSecurityPrivateKeyPath;

    @Bean
    JwtKeyStoreGenerator jwtKeyStoreGenerator(ResourceLoader resourceLoader) throws IOException{
        Resource resourcePrivate = resourceLoader.getResource(this.authSecurityPrivateKeyPath);
        JwtKeyStoreGenerator generator = new JwtKeyStoreGenerator();
        generator.addPrivateKeyPath(resourcePrivate.getFile().toPath());
        generator.addSecretKey(this.authSecuritySecret, "s");
        return generator;
    }

    @Bean
    JwtKeyStoreValidator jwtKeyStoreValidator(ResourceLoader resourceLoader) throws IOException{
        Resource resourcePublic = resourceLoader.getResource(this.authSecurityPrivateKeyPath);
        JwtKeyStoreValidator validator = new JwtKeyStoreValidator();
        validator.addPublicKeyPath(resourcePublic.getFile().getPath());
        validator.addSecretKey(this.authSecuritySecret, "s");
        return validator;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
