package com.ejetool.lib.youtube.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateYoutubeVideoParam {
    private String sourceId;
    private String sourceUrl;
    private String snapshotUrl;

    private YoutubeVideoSnippet snippet;
    private YoutubeVideoStatus status;
    
    public boolean isSnippet(){
        return this.snippet != null;
    }

    public boolean isStatus(){
        return this.status != null;
    }
}
