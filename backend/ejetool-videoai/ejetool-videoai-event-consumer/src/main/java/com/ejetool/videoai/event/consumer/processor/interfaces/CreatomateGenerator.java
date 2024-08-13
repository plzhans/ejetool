package com.ejetool.videoai.event.consumer.processor.interfaces;

import java.util.Optional;

import com.ejetool.lib.creatomate.dto.RenderResponse;
import com.ejetool.videoai.domain.entity.CreatomateProject;
import com.ejetool.videoai.domain.entity.CreatomateRender;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CreatomateGenerator {
    
    @RequiredArgsConstructor
    @Getter
    public static class CreatomateProjectTemplate{
        private final int id;
        private final String apiKey;
        private final String templateId;
    }

    Optional<CreatomateProject> getProject(int projectId);

    Optional<CreatomateRender> getRender(String renderId);

    CreatomateProjectTemplate getActiveProjectTemplate();

    void disableProject(int disableKeyId, String cause);

    void addRender(int id, RenderResponse renderResponse);

    CreatomateRender save(CreatomateRender render);

}
