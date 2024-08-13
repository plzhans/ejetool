package com.ejetool.account.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.ejetool.account"})
@EnableJpaRepositories(basePackages = {"com.ejetool.account"})
@SpringBootApplication(scanBasePackages = {
	"com.ejetool.core.api",
	"com.ejetool.account"
})
@EnableAspectJAutoProxy
public class EjetoolAccountApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjetoolAccountApiApplication.class, args);
	}

}