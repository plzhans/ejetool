    package com.ejetool.videoai.client;

import org.springframework.web.client.RestTemplate;
import com.ejetool.videoai.client.content.ContentApi;
import com.ejetool.videoai.client.content.ContentApiImpl;
import com.ejetool.videoai.client.setting.EjetoolVideoAIClientSetting;

public class EjetoolVideoAIClientImpl implements EjetoolVideoAIClient {

    private final EjetoolVideoAIClientSetting settings;
    private final RestTemplate restTemplate;

    public EjetoolVideoAIClientImpl(EjetoolVideoAIClientSetting settings){
        this.settings = settings;
        this.restTemplate = this.createRestTemplate();
    }
    
    private RestTemplate createRestTemplate(){
        return new RestTemplate();
    }

    public ContentApi content(){
        return new ContentApiImpl(this.settings, this.restTemplate);
    }
}
