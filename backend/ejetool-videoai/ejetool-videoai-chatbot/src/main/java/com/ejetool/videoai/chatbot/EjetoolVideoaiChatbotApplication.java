package com.ejetool.videoai.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	"com.ejetool.videoai",
	"com.ejetool.openai"
})
public class EjetoolVideoaiChatbotApplication {

	public static void main(String[] args){
		SpringApplication.run(EjetoolVideoaiChatbotApplication.class, args);
	}

}
