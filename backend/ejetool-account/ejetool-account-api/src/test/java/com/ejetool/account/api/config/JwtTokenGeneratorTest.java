package com.ejetool.account.api.config;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ejetool.account.service.config.JwtConfig;
import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.jwt.generator.JwtGenerator;
import com.ejetool.jwt.generator.JwtKeyType;
import com.ejetool.jwt.generator.JwtValidator;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
import com.ejetool.jwt.generator.JwtKeyStoreValidator;
import com.ejetool.jwt.generator.RSAJwtGenerator;
import com.ejetool.jwt.generator.RSAJwtValidator;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;

@SpringJUnitConfig(classes = {JwtTokenGeneratorTest.Config.class})
@TestPropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
class JwtTokenGeneratorTest {

    @TestConfiguration
    @EnableConfigurationProperties(AuthSecuritySettings.class)
    static class Config{
        
    }

    @Getter @Setter
    @ConfigurationProperties("auth.security")
    static class AuthSecuritySettings{

        @Value("${auth.security.issuer}")
        private String issuer;

        private String[] allowedIssuers;

        @Value("${auth.security.secret}")
        private String secret;

        @Value("${auth.security.private_key_path}")
        private String privateKeyPath;

        @Value("${auth.security.public_key_path}")
        private String publicKeyPath;
    }

    @Autowired
    private AuthSecuritySettings settings;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void rsa_generator_and_verify() throws IOException {
        String issuer = this.settings.getIssuer();
        String sub = "1";
        String name = "ejegong-ai";
        String[] roles = new String[]{"admin"};

        Resource resourcePrivate = resourceLoader.getResource(this.settings.getPrivateKeyPath());
        JwtGenerator generator = RSAJwtGenerator.create(
            resourcePrivate.getFile().toPath()
        );

        JwtValidator privateKeyValidator = RSAJwtValidator.create(
            resourcePrivate.getFile().toPath()
        );
        
        Resource resourcePublic = resourceLoader.getResource(this.settings.getPublicKeyPath());
        JwtValidator publicKeyValidator = RSAJwtValidator.create(
            resourcePublic.getFile().toPath()
        );

        String token = generator.builder(issuer, sub)
            .claim("name", name)
            .claim("roles", roles)
            .compact();

        Claims generatorVerifyClaims = generator.verify(token);
        Claims validatorPrivateKeyVerifyClaims = privateKeyValidator.verify(token);
        Claims validatorPublicKeyVerifyClaims = publicKeyValidator.verify(token);

        Assertions.assertFalse(token.isEmpty());
        
        Assertions.assertEquals(issuer, generatorVerifyClaims.getIssuer());
        Assertions.assertEquals(sub, generatorVerifyClaims.getSubject());
        Assertions.assertEquals(name, generatorVerifyClaims.get("name"));

        Assertions.assertEquals(issuer, validatorPrivateKeyVerifyClaims.getIssuer());
        Assertions.assertEquals(sub, validatorPrivateKeyVerifyClaims.getSubject());
        Assertions.assertEquals(name, validatorPrivateKeyVerifyClaims.get("name"));

        Assertions.assertEquals(issuer, validatorPublicKeyVerifyClaims.getIssuer());
        Assertions.assertEquals(sub, validatorPublicKeyVerifyClaims.getSubject());
        Assertions.assertEquals(name, validatorPublicKeyVerifyClaims.get("name"));
    }


    @Test
    void keyStore_generator_and_verify() throws IOException {
        String issuer = this.settings.getIssuer();
        String sub = "1";
        String name = "ejegong-ai";
        String[] roles = new String[]{"admin"};

        Resource resourcePrivate = resourceLoader.getResource(this.settings.getPrivateKeyPath());
        JwtKeyStoreGenerator generator = JwtKeyStoreGenerator.build(o->o.setIssuer(issuer));
        generator.addPrivateKeyPath(resourcePrivate.getFile().toPath());
        generator.addSecretKey(this.settings.getSecret(), "s");
        
        Resource resourcePublic = resourceLoader.getResource(this.settings.getPublicKeyPath());
        JwtKeyStoreValidator validator = JwtKeyStoreValidator.build(o->o.addIssuer(this.settings.getAllowedIssuers()));
        validator.addPublicKeyPath(resourcePublic.getFile().getPath());
        validator.addSecretKey(this.settings.getSecret(), "s");

        String token = generator.builder(JwtKeyType.SYMMETRIC, sub)
            .setIssuer(issuer)
            .claim("name", name)
            .claim("roles", roles)
            .compact();

        Claims validatorVerifyClaims = validator.verify(token);

        Assertions.assertFalse(token.isEmpty());
        
        Assertions.assertEquals(issuer, validatorVerifyClaims.getIssuer());
        Assertions.assertEquals(sub, validatorVerifyClaims.getSubject());
        Assertions.assertEquals(name, validatorVerifyClaims.get("name"));
    }
}
