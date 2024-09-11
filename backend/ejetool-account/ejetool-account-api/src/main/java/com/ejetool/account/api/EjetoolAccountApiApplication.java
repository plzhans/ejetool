package com.ejetool.account.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	"com.ejetool.core.api",
	"com.ejetool.account"
})
public class EjetoolAccountApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjetoolAccountApiApplication.class, args);
	}

}