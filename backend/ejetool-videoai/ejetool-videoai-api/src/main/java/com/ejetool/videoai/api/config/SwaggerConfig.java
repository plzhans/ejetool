package com.ejetool.videoai.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerConfig.Settings.class)
public class SwaggerConfig{

    @ConfigurationProperties("springdoc")
    public static class Settings {
        @Getter  @Setter
        @Value("${springdoc.api-servers}")
        private String[] apiServers;
    }
    private final Settings settings;

    @PostConstruct
	public void initialize(){
        if(settings.getApiServers() != null && settings.getApiServers().length > 0){
            for(String server : settings.getApiServers()){
                log.info("SwaggerDoc server added. {}", server);
            }
        }
    }

    @Bean
    Info info() {
        return new Info()
            .title("VideoAI API")
            .description("Video AI API") // API에 대한 설명
            .version("v1"); // API의 버전
    }

    /**
     * OpenAPI 문서
     * - issue : @Bean 등록했는데 여러번 호출된다...OTL
     * @param info
     * @return
     */
    @Bean
    OpenAPI swaggerOpenAPI(Info info) {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
            .addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"))
            ;

        var openAPI = new OpenAPI()
                .components(new Components())
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
        
        if(settings.getApiServers() != null && settings.getApiServers().length > 0){
            for(String server : settings.getApiServers()){
                String[] split = server.split(";", 2);
                var serverItem = new Server().url(split[0]);
                if(split.length > 1){
                    serverItem.setDescription(split[1]);
                }
                openAPI.addServersItem(serverItem);
            }
        }
        return openAPI;
    }
}
