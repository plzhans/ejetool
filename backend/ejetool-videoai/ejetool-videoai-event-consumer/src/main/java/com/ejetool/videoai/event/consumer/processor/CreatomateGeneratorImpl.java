package com.ejetool.videoai.event.consumer.processor;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ejetool.lib.creatomate.dto.RenderResponse;
import com.ejetool.videoai.domain.entity.CreatomateProject;
import com.ejetool.videoai.domain.entity.CreatomateRender;
import com.ejetool.videoai.domain.repository.CreatomateProjectRepository;
import com.ejetool.videoai.domain.repository.CreatomateRenderRepository;
import com.ejetool.videoai.event.consumer.exception.VideoGenerateException;
import com.ejetool.videoai.event.consumer.processor.interfaces.CreatomateGenerator;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@Service
@RequiredArgsConstructor
public class CreatomateGeneratorImpl implements CreatomateGenerator{

    @UtilityClass
    private static class Const {
        static final String CACHE_KEY = "creatomate:actived-project";
    }

    private final CreatomateProjectRepository projectRepository;
    private final CreatomateRenderRepository renderRepository;
    
    @Cacheable(Const.CACHE_KEY)
    public CreatomateProjectTemplate getActiveProjectTemplate(){
        var project = this.projectRepository.findFirstByEnabled()
            .orElseThrow(()->new VideoGenerateException("creatomate project notfound."));
        if(!project.isEnabled()){
            project.setDisabled("disabled.");
            this.projectRepository.save(project);
            throw new VideoGenerateException(String.format("creatomate project disabled. id=%s", project.getId()));
        }
        if(!StringUtils.hasText(project.getApiKey())){
            project.setDisabled("api_key empty.");
            this.projectRepository.save(project);
            throw new VideoGenerateException(String.format("creatomate project api_key empty. id=%s", project.getId()));
        }
        if(!StringUtils.hasText(project.getTemplateId())){
            project.setDisabled("template_id empty.");
            this.projectRepository.save(project);
            throw new VideoGenerateException(String.format("creatomate project template_id empty. id=%s", project.getId()));
        }
        return new CreatomateProjectTemplate(project.getId(), project.getApiKey(), project.getTemplateId());
    }

    @CacheEvict(value=Const.CACHE_KEY, allEntries= true)
    public void disableProject(int disableKeyId, String cause){
        this.projectRepository.findById(disableKeyId)
            .ifPresent(x->{
                x.setDisabled(cause);
                this.projectRepository.save(x);
            });
    }


    private CreatomateRender createCreatomateRenderEntity(int projectId, RenderResponse response){
        return CreatomateRender.builder()
            .id(response.getId())
            .projectId(projectId)
            .status(response.getStatus())
            .errorMessage(response.getErrorMessage())
            .url(response.getUrl())
            .snapshotUrl(response.getSnapshotUrl())
            .templateId(response.getTemplateId())
            .templateName(response.getTemplateName())
            .templateTags(String.join(",", response.getTemplateTags()))
            .outputFormat(response.getOutputFormat())
            .renderScale(response.getRenderScale())
            .width(response.getWidth())
            .height(response.getHeight())
            .frameRate(response.getFrameRate())
            .duration(response.getDuration())
            .fileSize(response.getFileSize())
            .webhookUrl(response.getWebhookUrl())
            .modifications(response.getModifications())
            .build();
    }

    @Override
    public Optional<CreatomateProject> getProject(int projectId){
        return this.projectRepository.findById(projectId);
    }

    @Override
    public void addRender(int keyId, RenderResponse renderResponse) {
        var render = this.createCreatomateRenderEntity(keyId, renderResponse);
        this.renderRepository.save(render);
    }

    @Override
    public Optional<CreatomateRender> getRender(String renderId) {
        return this.renderRepository.findById(renderId);
    }

    @Override
    public CreatomateRender save(CreatomateRender render){
        return this.renderRepository.save(render);
    }
}
