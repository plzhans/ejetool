package com.ejetool.lib.creatomate.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRenderRequest {

    @JsonProperty("template_id")
    private String templateId;
    
    /**
     * jpg, png, gif, mp4
     */
    @JsonProperty("output_format")
    private String outputFormat;

    @JsonProperty("frame_rate")
    private Integer frameRate;

    @JsonProperty("render_scale")
    private Integer renderScale;

    @JsonProperty("max_width")
    private Integer maxWidth;

    @JsonProperty("max_height")
    private Integer maxHeight;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("modifications")
    private Map<String, String> modifications;

    @JsonProperty("webhook_url")
    private String webhookUrl;

    @JsonProperty("metadata")
    private String metadata;

    public static CreateRenderRequest createVideo(){
        var inst = new CreateRenderRequest();
        inst.outputFormat = "mp4";
        return inst;
    }

    public void addModification(String key, String value){
        if(this.modifications == null){
            this.modifications = new HashMap<>();
        }
        this.modifications.put(key, value);
    }
    
}