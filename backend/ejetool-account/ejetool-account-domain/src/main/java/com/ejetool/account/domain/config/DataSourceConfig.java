package com.ejetool.account.domain.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataSourceConfig{
   
    @Bean
    DataSource dataSource(DataSourceProperties properties, ResourceLoader resourceLoader) throws IOException {
        if(properties.getPassword() != null && properties.getPassword().startsWith("file:")){
            var file = resourceLoader.getResource(properties.getPassword()).getFile();
            if(!file.exists()){
                throw new FileNotFoundException(file.getPath());
            }
            String password = new String(Files.readAllBytes(file.toPath()));
            properties.setPassword(password);
        }
        var dataSource = properties.initializeDataSourceBuilder().build();
        return dataSource;
    }
}