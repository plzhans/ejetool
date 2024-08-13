package com.ejetool.videoai.api;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.ejetool.videoai.api.config.SwaggerConfig;
import com.ejetool.videoai.service.application.ContentService;
import com.ejetool.videoai.service.application.ContentServiceImpl;
import com.ejetool.videoai.service.application.GptService;
import com.ejetool.videoai.service.application.GptServiceImpl;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("openapi")
@SpringBootApplication(
	scanBasePackages = {
		"com.ejetool.core.api",
		"com.ejetool.videoai.api.controller",
		"com.ejetool.videoai.api.mapper"
	},
	exclude = {
		DataSourceAutoConfiguration.class
	}
)
@Import({SwaggerConfig.class})
@RequiredArgsConstructor
@Slf4j
public class EjetoolVideoaiOpenApi implements ApplicationRunner {

	private final Environment environment;

	@Bean
	ContentService contentService(){
		return new ContentServiceImpl(null, null, null, null, null, null);
	}

	@Bean
	GptService gptService(){
		return new GptServiceImpl();
	}

	@Bean
    SecurityFilterChain permitAll(HttpSecurity http) throws Exception {
        http
            .csrf(x->x.disable())
            .headers(x->x.frameOptions(o->o.sameOrigin()))
            .sessionManagement(x->x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry -> 
                registry.anyRequest().permitAll())
            ;
        return http.build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Active profiles : "+ Arrays.toString(environment.getActiveProfiles()));

        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String serverUrl = "http://localhost:" + port + contextPath+"/docs/";
        log.info("Server url: " + serverUrl);
    }
}