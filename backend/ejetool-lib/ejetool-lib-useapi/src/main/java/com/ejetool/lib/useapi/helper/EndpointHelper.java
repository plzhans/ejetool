package com.ejetool.lib.useapi.helper;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EndpointHelper {

    /**
     * 
     * @param path
     * @return
     */
    public static String endpoint(String host, String path){
        return UriComponentsBuilder
            .fromUriString(host)
            .path(path)
            .build()
            .toString();
    }

    /**
     * 
     * @param path
     * @return
     */
    public static URI endpointUri(String host, String path){
        return UriComponentsBuilder
            .fromUriString(host)
            .path(path)
            .build()
            .toUri();
    }

    /**
     * 
     * @param path
     * @return
     */
    public static UriComponentsBuilder endpointUriBuilder(String host, String path){
        return UriComponentsBuilder
            .fromUriString(host)
            .path(path);
    }
}
