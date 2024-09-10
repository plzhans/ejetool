package com.ejetool.account.service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;

import lombok.Getter;
import lombok.Setter;

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

    @Getter @Setter
    @ConfigurationProperties("auth.security")
    static class AuthSecuritySettings{

        @Value("${auth.security.issuer}")
        private String issuer;

        @Value("${auth.security.public_key_path}")
        private String publicKeyPath;
    }

     @Autowired
    private AuthSecuritySettings settings;

    @Autowired
    private ResourceLoader resourceLoader;

    private JwtKeyStoreGenerator jwtKeyStoreGenerator() throws IOException{
        Resource resourcePublic = resourceLoader.getResource(this.settings.getPublicKeyPath());
        JwtKeyStoreGenerator generator = JwtKeyStoreGenerator.build(o->o.setIssuer(this.settings.getIssuer()));
        generator.addPublicKeyPath(resourcePublic.getFile().toPath(), "s");
        return generator;
    }

    @Test
    void getPublicKeys_ok() throws IOException {
        Resource resourcePublic = resourceLoader.getResource(this.settings.getPublicKeyPath());

        String fileText = Files.readString(resourcePublic.getFile().toPath());
        fileText = fileText.replace("\n", "\\n");

        var service = new AuthServiceImpl(jwtKeyStoreGenerator());
        var result = service.getPublicKeys();
        
        assertEquals(result.getPublicKeys().get(0).getId(), "s");
        assertEquals(result.getPublicKeys().get(0).getPublicKey(), fileText);
    }

}
