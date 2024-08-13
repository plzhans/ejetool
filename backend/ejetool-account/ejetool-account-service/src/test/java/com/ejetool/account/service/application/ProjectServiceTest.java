package com.ejetool.account.service.application;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
import com.ejetool.account.service.dto.project.CreateProjectApiKeyParam;

@SpringJUnitConfig(ProjectServiceTest.Config.class)
@TestPropertySource(locations = "file:../ejetool-account-api/src/main/resources/application.yml", factory = YamlPropertySourceFactory.class)
@ContextConfiguration
class ProjectServiceTest {

    @TestConfiguration
    static class Config{
        @Value("${auth.security.secret}")
        private String authSecuritySecret;
    }

    @Autowired
    private Config config;

    private JwtKeyStoreGenerator jwtKeyStoreGenerator(){
        JwtKeyStoreGenerator generator =  new JwtKeyStoreGenerator();
        generator.addSecretKey(this.config.authSecuritySecret, "s");
        return generator;
    }

    @Test
    void createApiKey() {
        var service = new ProjectServiceImpl(jwtKeyStoreGenerator());

        var param = CreateProjectApiKeyParam.builder()
            .projectId(1)
            .build();

        var result = service.createApiKey(param);

        assertFalse(result.getApiKey().isEmpty());
    }
}
