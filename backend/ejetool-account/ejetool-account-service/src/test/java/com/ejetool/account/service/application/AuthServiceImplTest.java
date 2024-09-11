package com.ejetool.account.service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ejetool.account.service.config.JwtConfig.AuthSecuritySettings;
import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
import com.ejetool.jwt.generator.JwtKeyStoreValidator;

@SpringJUnitConfig(ProjectServiceTest.Config.class)
@TestPropertySource(locations = {
    "file:../ejetool-account-api/src/main/resources/application.yml",
    "classpath:application.yml"
    }, factory = YamlPropertySourceFactory.class)
@ContextConfiguration
public class AuthServiceImplTest {

    @TestConfiguration
    @EnableConfigurationProperties(AuthSecuritySettings.class)
    static class Config{
        
    }

    @Autowired
    private AuthSecuritySettings settings;

    @Autowired
    private ResourceLoader resourceLoader;

    private JwtKeyStoreGenerator jwtKeyStoreGenerator() throws IOException{
        Resource resourcePrivate = resourceLoader.getResource(this.settings.getPrivateKeyPath());
        JwtKeyStoreGenerator generator = JwtKeyStoreGenerator.build(o->{
            o.setIssuer(this.settings.getIssuer());
        });
        generator.addPrivateKeyPath(resourcePrivate.getFile().toPath());
        generator.addSecretKey(this.settings.getSecret(), "s");
        return generator;
    }

    @Test
    void getPublicKeyList__ok() throws IOException {
        Resource publicKeyPath = resourceLoader.getResource(this.settings.getPublicKeyPath());

        String publicKeyString = Files.readString(publicKeyPath.getFile().toPath());
        publicKeyString = publicKeyString.replace("\n", "\\n");

        var service = new AuthServiceImpl(this.settings.getIssuer(), jwtKeyStoreGenerator());
        var result = service.getPublicKeyList();
        
        assertEquals(result.getIssuer(), this.settings.getIssuer());
        assertEquals(result.getKeys().get(0).getId(), JwtKeyStoreValidator.DEFAULT_KID);
        assertEquals(result.getKeys().get(0).getContent(), publicKeyString);
    }

}
