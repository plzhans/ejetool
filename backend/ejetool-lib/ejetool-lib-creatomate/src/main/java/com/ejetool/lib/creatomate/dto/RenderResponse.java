package com.ejetool.lib.creatomate.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * {
 *  "id": "69ba407b-72a9-4050-a9fb-559827b9623d",
 *  "status": "succeeded",
 *  "url": "https://cdn.creatomate.com/renders/69ba407b-72a9-4050-a9fb-559827b9623d.mp4",
 *  "snapshot_url": "https://cdn.creatomate.com/snapshots/69ba407b-72a9-4050-a9fb-559827b9623d.jpg",
 *  "template_id": "9e90d011-52e6-49dc-8a7a-5f25058c2568",
 *  "template_name": "Your Template Name",
 *  "template_tags": [],
 *  "output_format": "mp4",
 *  "render_scale": 1,
 *  "width": 720,
 *  "height": 900,
 *  "frame_rate": 60,
 *  "duration": 15.5,
 *  "file_size": 751089
 *  }
 */
@Data
public class RenderResponse{
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("url")
    private String url;

    @JsonProperty("snapshot_url")
    private String snapshotUrl;

    @JsonProperty("template_id")
    private String templateId;

    @JsonProperty("template_name")
    private String templateName;

    @JsonProperty("template_tags")
    private String[] templateTags;
    
    @JsonProperty("output_format")
    private String outputFormat;

    @JsonProperty("render_scale")
    private int renderScale;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    @JsonProperty("frame_rate")
    private int frameRate;

    @JsonProperty("duration")
    private float duration;

    @JsonProperty("file_size")
    private long fileSize;

    @JsonProperty("webhook_url")
    private String webhookUrl;

    @JsonProperty("modifications")
    private Map<String, String> modifications;
}
