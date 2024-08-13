package com.ejetool.videoai.api.runner;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MainRunner implements ApplicationRunner {

    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Active profiles : "+ Arrays.toString(environment.getActiveProfiles()));

        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String serverUrl = "http://localhost:" + port + contextPath;
        log.info("Server url: " + serverUrl);
    }
    
}
