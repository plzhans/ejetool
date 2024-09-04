package com.ejetool.account.service.application;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;

import lombok.Getter;
import lombok.Setter;

import com.ejetool.account.service.dto.project.CreateProjectApiKeyParam;

@SpringJUnitConfig(ProjectServiceTest.Config.class)
@TestPropertySource(locations = "file:../ejetool-account-api/src/main/resources/application.yml", factory = YamlPropertySourceFactory.class)
//@TestPropertySource(locations = "file:../ejetool-account-api/src/main/resources/application-local.yml", factory = YamlPropertySourceFactory.class)
@ContextConfiguration
class ProjectServiceTest {
    @TestConfiguration
    @EnableConfigurationProperties(AuthSecuritySettings.class)
    static class Config{
        
    }

    @Getter @Setter
    @ConfigurationProperties("auth.security")
    static class AuthSecuritySettings{

        @Value("${auth.security.issuer}")
        private String issuer;

        @Value("${auth.security.secret}")
        private String secret;
    }

    @Autowired
    private AuthSecuritySettings settings;

    private JwtKeyStoreGenerator jwtKeyStoreGenerator(){
        JwtKeyStoreGenerator generator = JwtKeyStoreGenerator.build(o->o.setIssuer(this.settings.getIssuer()));
        generator.addSecretKey(this.settings.getSecret(), "s");
        return generator;
    }

    @Test
    void createApiKey() {
        var service = new ProjectServiceImpl(jwtKeyStoreGenerator());

        var param = CreateProjectApiKeyParam.builder()
            .projectId(1)
            .build();

        var result = service.createApiKey(param);
        //System.out.println("ApiKey : " + result.getApiKey());
        assertFalse(result.getApiKey().isEmpty());
    }
}
