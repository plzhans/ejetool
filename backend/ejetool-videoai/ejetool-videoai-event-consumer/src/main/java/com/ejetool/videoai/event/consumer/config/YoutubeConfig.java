package com.ejetool.videoai.event.consumer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.youtube.service.YoutubeService;
import com.ejetool.lib.youtube.service.YoutubeServiceMakeProxyImpl;
import com.ejetool.lib.youtube.setting.MakeYoutubeWebhookSettings;
import com.ejetool.videoai.event.consumer.setting.GoogleDriveSettings;

@Configuration
@EnableConfigurationProperties({
    GoogleDriveSettings.class,
    MakeYoutubeWebhookSettings.class
})
public class YoutubeConfig {

    // @Bean
    // YouTube youtube(GoogleCredentials googleCredentials) throws GeneralSecurityException, IOException{
    //     final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
    //     final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        
    //     HttpCredentialsAdapter httpCredentialsAdapter = new HttpCredentialsAdapter(googleCredentials);
    //     YouTube youtube = new YouTube.Builder(transport, jsonFactory, httpCredentialsAdapter)
    //         .setApplicationName("ejegong-creator-ai")
    //         .build();
    //     return youtube;
    // }

    // @Bean
    // YoutubeService youtubeService(YouTube youTube){
    //     return new YoutubeServiceImpl(youTube);
    // }

    // @Bean
    // YoutubeService youtubeService(SocialBufferSettings socialBufferSettings){
    //     return new YoutubeServiceMakeProxyImpl(socialBufferSettings);
    // }

    @Bean
    YoutubeService youtubeService(MakeYoutubeWebhookSettings settings){
        return new YoutubeServiceMakeProxyImpl(settings);
    }
}
