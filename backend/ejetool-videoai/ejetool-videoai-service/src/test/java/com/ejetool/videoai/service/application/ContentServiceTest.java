package com.ejetool.videoai.service.application;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.kafka.common.Uuid;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import com.ejetool.common.io.support.YamlPropertySourceFactory;
import com.ejetool.lib.openai.OpenaiModelGPT;
import com.ejetool.lib.telegram.TelegramBotSettings;
import com.ejetool.videoai.common.content.ContentType;
import com.ejetool.videoai.service.dto.content.ContentCreateByAIParam;

@SpringBootTest(classes = ContentServiceTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAspectJAutoProxy
@EnableCaching
@EntityScan(basePackages = {"com.ejetool.videoai"})
@EnableJpaRepositories(basePackages = {"com.ejetool.videoai"})
@TestPropertySource(value = {
        "file:../ejetool-videoai-api/src/main/resources/application.yml",
        "file:../ejetool-videoai-api/src/main/resources/application-local.yml",
        "classpath:application.yml"
    }, factory = YamlPropertySourceFactory.class)
@Disabled("local test")
class ContentServiceTest {

    @SpringBootConfiguration
    @ComponentScan(basePackages = "com.ejetool.videoai")
    static class TestConfig {

    }

    @Autowired
    private TelegramBotSettings telegramBotSettings;

    @Autowired
    private ContentService contentService;

    @Test
    @Disabled("local test")
    void testCreateByAI() {
        var param = new ContentCreateByAIParam();
        param.setRequestId(Uuid.randomUuid().toString().replace("-", ""));
        param.setContentType(ContentType.QUOTE);
        param.setTelegramChatId(telegramBotSettings.getBotDefaultChatId());
        param.setSubject("자신감");
        param.setGptModel(OpenaiModelGPT.GPT_4_TURBO);
        
        var result = this.contentService.createByAI(param);

        assertTrue(result.getContentId() > 0);
    }
}
