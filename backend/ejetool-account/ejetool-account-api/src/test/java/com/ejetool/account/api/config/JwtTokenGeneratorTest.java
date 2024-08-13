package com.ejetool.account.api.config;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.jwt.generator.JwtGenerator;
import com.ejetool.jwt.generator.JwtKeyType;
import com.ejetool.jwt.generator.JwtValidator;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
import com.ejetool.jwt.generator.JwtKeyStoreValidator;
import com.ejetool.jwt.generator.RSAJwtGenerator;
import com.ejetool.jwt.generator.RSAJwtValidator;

import io.jsonwebtoken.Claims;

@SpringJUnitConfig(classes = {JwtTokenGeneratorTest.Config.class})
@TestPropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
class JwtTokenGeneratorTest {

    @TestConfiguration
    static class Config{
        @Value("${auth.security.secret}")
        private String authSecuritySecret;

        @Value("${auth.security.private_key_path}")
        private String authSecurityPrivateKeyPath;

        @Value("${auth.security.public_key_path}")
        private String authSecurityPublicKeyPath;
    }

    @Autowired
    private Config config;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void rsa_generator_and_verify() throws IOException {
        String issuer = "https://projects.ejetool.com";
        String sub = "1";
        String name = "ejegong-ai";
        String[] roles = new String[]{"admin"};

        Resource resourcePrivate = resourceLoader.getResource(this.config.authSecurityPrivateKeyPath);
        JwtGenerator generator = RSAJwtGenerator.create(
            resourcePrivate.getFile().toPath()
        );

        JwtValidator privateKeyValidator = RSAJwtValidator.create(
            resourcePrivate.getFile().toPath()
        );
        
        Resource resourcePublic = resourceLoader.getResource(this.config.authSecurityPublicKeyPath);
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
        String issuer = "https://projects.ejetool.com";
        String sub = "1";
        String name = "ejegong-ai";
        String[] roles = new String[]{"admin"};

        Resource resourcePrivate = resourceLoader.getResource(this.config.authSecurityPrivateKeyPath);
        JwtKeyStoreGenerator generator = new JwtKeyStoreGenerator();
        generator.addPrivateKeyPath(resourcePrivate.getFile().toPath());
        generator.addSecretKey(this.config.authSecuritySecret, "s");
        
        Resource resourcePublic = resourceLoader.getResource(this.config.authSecurityPublicKeyPath);
        JwtKeyStoreValidator validator = new JwtKeyStoreValidator();
        validator.addPublicKeyPath(resourcePublic.getFile().getPath());
        validator.addSecretKey(this.config.authSecuritySecret, "s");

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
