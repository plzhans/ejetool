package com.ejetool.videoai.event.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy
@EntityScan(basePackages = {"com.ejetool.videoai"})
@EnableJpaRepositories(basePackages = {"com.ejetool.videoai"})
@SpringBootApplication
public class EjetoolVideoaiEventConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjetoolVideoaiEventConsumerApplication.class, args);
	}

}
