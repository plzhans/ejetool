package com.ejetool.account.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.ejetool.account"})
@EnableJpaRepositories(basePackages = {"com.ejetool.account"})
@Configuration
public class JpaConfig {
    
}
