package com.ejetool.videoai.api;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;

@EntityScan(basePackages = {"com.ejetool.videoai"})
@EnableJpaRepositories(basePackages = {"com.ejetool.videoai"})
@SpringBootApplication(scanBasePackages = {
	"com.ejetool.core.api",
	"com.ejetool.videoai"
})
@EnableAspectJAutoProxy
public class EjetoolVideoaiApiApplication {

	public static void main(String[] args) {
		if(Arrays.stream(args).anyMatch(x->StringUtils.hasText("--openapi"))){
			SpringApplication app = new SpringApplication(EjetoolVideoaiOpenApi.class);
			app.setAdditionalProfiles("openapi");
			app.run(args);
			return;
		}
		SpringApplication.run(EjetoolVideoaiApiApplication.class, args);
	}

}