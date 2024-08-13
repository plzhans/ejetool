package com.ejetool.videoai.client;

import com.ejetool.videoai.client.content.ContentApi;
import com.ejetool.videoai.client.setting.EjetoolVideoAIClientSetting;

public interface EjetoolVideoAIClient {

    ContentApi content();

    public static EjetoolVideoAIClient build(EjetoolVideoAIClientSetting settings){
        return new EjetoolVideoAIClientImpl(settings);
    }
    
}
