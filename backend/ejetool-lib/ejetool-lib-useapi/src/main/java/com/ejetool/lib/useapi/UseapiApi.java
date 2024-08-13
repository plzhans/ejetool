package com.ejetool.lib.useapi;

import org.springframework.stereotype.Component;

import com.ejetool.lib.useapi.midjourney.UseapiMidjourneyApi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UseapiApi {

    @Getter
    private final UseapiSettings settings;

    private UseapiMidjourneyApi midjourney;

    public synchronized UseapiMidjourneyApi midjourney(){
        if(this.midjourney == null){
            synchronized (this) {
                if (this.midjourney == null) {
                    this.midjourney = new UseapiMidjourneyApi(
                        this.settings.getToken(),
                        this.settings.getMidjourney()
                    );
                }
            }
        }
        return this.midjourney;
    }

}
