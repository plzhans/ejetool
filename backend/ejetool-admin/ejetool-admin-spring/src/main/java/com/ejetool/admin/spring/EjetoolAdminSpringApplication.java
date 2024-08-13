package com.ejetool.admin.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class EjetoolAdminSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjetoolAdminSpringApplication.class, args);
	}

}