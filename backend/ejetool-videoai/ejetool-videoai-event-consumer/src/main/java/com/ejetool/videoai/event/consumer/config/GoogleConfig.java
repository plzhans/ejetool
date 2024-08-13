package com.ejetool.videoai.event.consumer.config;

import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.videoai.event.consumer.setting.GoogleDriveSettings;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.api.client.json.JsonFactory;

@Configuration
@EnableConfigurationProperties(GoogleDriveSettings.class)
public class GoogleConfig {

    @Bean
    GoogleCredentials credentials(@Value("${google.service_account}") String serviceAccountJson) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(serviceAccountJson.getBytes());
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(inputStream)
                .createScoped(Arrays.asList(
                    "https://www.googleapis.com/auth/cloud-platform",
                    DriveScopes.DRIVE_FILE,
                    YouTubeScopes.YOUTUBE_UPLOAD
                ));
        return credentials;
    }

    @Bean
    TextToSpeechClient textToSpeechClient(GoogleCredentials googleCredentials) throws IOException{
        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(googleCredentials))
                .build();
        TextToSpeechClient client = TextToSpeechClient.create(settings);
        return client;
    }

    @Bean
    Drive googleDrive(GoogleCredentials googleCredentials) throws GeneralSecurityException, IOException{
        final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        HttpCredentialsAdapter httpCredentialsAdapter = new HttpCredentialsAdapter(googleCredentials);
        Drive drive = new Drive.Builder(transport, jsonFactory, httpCredentialsAdapter)
            .setApplicationName("ejegong-creator-ai")
            .build();
            
        return drive;
    }

}
