package com.ejetool.account.service.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ejetool.common.util.StringMakerUtils;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
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
        @Value("${auth.security.issuer}")
        private String issuer;
    
        @Getter @Setter
        private String[] allowedIssuers;
    
        @Getter @Setter
        @Value("${auth.security.secret}")
        private String secret;
    
        @Getter @Setter
        @Value("${auth.security.private_key_path}")
        private String privateKeyPath;

        @Getter @Setter
        @Value("${auth.security.public_key_path}")
        private String publicKeyPath;
    }
    private final AuthSecuritySettings settings;

    @Bean
    JwtKeyStoreGenerator jwtKeyStoreGenerator(ResourceLoader resourceLoader) throws IOException{
        log.info("settings.issuer={}", settings.getIssuer());
        log.info("settings.allowedIssuers={}", String.join(",", settings.getAllowedIssuers()));
        log.info("settings.privateKeyPath={}", settings.getPrivateKeyPath());
        log.info("settings.secret={}", StringMakerUtils.mask(settings.getSecret()));

        Resource resourcePrivate = resourceLoader.getResource(this.settings.getPrivateKeyPath());
        JwtKeyStoreGenerator generator = JwtKeyStoreGenerator.build(o->{
            o.setIssuer(this.settings.getIssuer());
        });
        generator.addPrivateKeyPath(resourcePrivate.getFile().toPath());
        generator.addSecretKey(this.settings.getSecret(), "s");
        return generator;
    }

    @Bean
    JwtKeyStoreValidator jwtKeyStoreValidator(ResourceLoader resourceLoader) throws IOException{
        Resource resourcePublic = resourceLoader.getResource(this.settings.getPrivateKeyPath());
        JwtKeyStoreValidator validator = JwtKeyStoreValidator.build(o->{
            o.addIssuer(this.settings.getAllowedIssuers());
        });
        validator.addPublicKeyPath(resourcePublic.getFile().getPath());
        validator.addSecretKey(this.settings.getSecret(), "s");
        return validator;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
