package com.ejetool.lib.youtube.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.ejetool.lib.youtube.consts.YoutubeVideoPart;
import com.ejetool.lib.youtube.dto.CreateYoutubeVideoParam;
import com.ejetool.lib.youtube.dto.YoutubeVideo;
import com.ejetool.lib.youtube.exception.YoutubeExecuteException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@RequiredArgsConstructor
public class YoutubeServiceImpl implements YoutubeService {
    
    private final YouTube youtube;

    public Video createYoutubeVideo(CreateYoutubeVideoParam param, List<String> part){
        Video video = new Video();
        if(param.isSnippet()){
            VideoSnippet snippet = new VideoSnippet();
            snippet.setTags(param.getSnippet().getTags());
            snippet.setCategoryId(param.getSnippet().getCategoryId());
            snippet.setTitle(param.getSnippet().getTitle());
            snippet.setDescription(param.getSnippet().getDescription());

            video.setSnippet(snippet);
        }
        if(param.isStatus()){
            VideoStatus status = new VideoStatus();
            status.setLicense(param.getStatus().getLicense());
            status.setPrivacyStatus(param.getStatus().getPrivacyStatus());
            status.setEmbeddable(param.getStatus().getEmbeddable());
            status.setPublicStatsViewable(param.getStatus().getPublicStatsViewable());
            status.setMadeForKids(param.getStatus().getMadeForKids());
            
            video.setStatus(status);
        }
        part.add(YoutubeVideoPart.SNIPPET);
        part.add(YoutubeVideoPart.STATISTICS);
        part.add(YoutubeVideoPart.STATUS);
        return video;
    }
    
    public YoutubeVideo insert(CreateYoutubeVideoParam param){
        
        List<String> part = new ArrayList<>();
        var newVideo = this.createYoutubeVideo(param, part);
        try{
            URLConnection fileConnection = URI.create(param.getSourceUrl()).toURL().openConnection();
            try(InputStream inputStream = fileConnection.getInputStream()){
                InputStreamContent mediaContent = new InputStreamContent("video/*", inputStream);
                mediaContent.setLength(fileConnection.getContentLength());
            
                    var insert = this.youtube.videos()
                        .insert(part, newVideo, mediaContent);
                    Video video = insert.execute();

                    YoutubeVideo data = new YoutubeVideo();
                    data.setId(video.getId());
                    return data;
                
            }
        } catch(GoogleJsonResponseException e){
            log.error("youtube.videos().insert(): GoogleJsonResponseException.", e);
            throw new YoutubeExecuteException(String.format("youtube.videos().insert(): GoogleJsonResponseException.", param.getSourceId()), e);
        } catch(IOException e){
            log.error("youtube.videos().insert(): IOException.", e);
            throw new YoutubeExecuteException(String.format("youtube.videos().insert(): IOException.", param.getSourceId()), e);
        }catch(Exception e){
            log.error("youtube.videos().insert(): Exception.", e);
            throw new YoutubeExecuteException(String.format("youtube.videos().insert(): Exception.", param.getSourceId()), e);
        }
    }
}
