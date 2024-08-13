package com.ejetool.videoai.event.consumer.processor;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ejetool.lib.creatomate.CreatomateApi;
import com.ejetool.lib.creatomate.dto.CommonResponse;
import com.ejetool.lib.creatomate.dto.CreateRenderRequest;
import com.ejetool.lib.creatomate.dto.RenderStatus;
import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.lib.creatomate.dto.RenderResponse;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.domain.entity.CreatomateProject;
import com.ejetool.videoai.domain.repository.ContentRepository;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoParam;
import com.ejetool.videoai.event.consumer.exception.VideoGenerateException;
import com.ejetool.videoai.event.consumer.processor.interfaces.CreatomateGenerator;
import com.ejetool.videoai.event.consumer.processor.interfaces.CreatomateGenerator.CreatomateProjectTemplate;
import com.ejetool.videoai.event.consumer.processor.interfaces.VideoGenerateProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoGenerateProcessorImpl implements VideoGenerateProcessor {
    
    private final CreatomateApi creatomateApi;
    private final CreatomateGenerator creatomateGenerator;
    private final ContentRepository contentRepository;

    /**
     * 실행
     */
    @Override 
    public boolean generate(Content content, GenerateVideoParam param, TelegramChatNotify telegramNotify){
        log.info("generate(): start. contentId={}, failRetry={}", content.getId(), param.isFailRetry());
        boolean result = true;
        while(result){
            if(content.isVideoFinish(param.isFailRetry())){
                result = false;
                break;
            }

            int wait = this.next(content, param.isFailRetry());
            if(wait > 0){
                try {
                    Thread.sleep(Duration.ofSeconds(wait).toMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("generate(): InterruptedException.", e);
                }
            } else if (wait < 0){
                result = false; 
            }
        }
        log.info("generate(): end. contentId={}, result={}", content.getId(), result);
        return result;
    }

    private int next(Content content, boolean failRetry){
        log.info("next(): start. contentId={}", content.getId());
        try {
            switch (content.getVideoStatus()) {
                case NONE:
                case READY:
                case REQUEST:
                    return this.stepRequest(content);
                case PROCESS:
                    return this.stepProcess(content);
                case FAILED:
                    if(failRetry){
                        if(StringUtils.hasText(content.getVideoTxId())){
                            content.resetVideoReady();
                            this.contentRepository.save(content);
                            return this.stepRequest(content);
                        } else {
                            content.setVideoProcess(content.getVideoTxId());
                            this.contentRepository.save(content);
                            return this.stepRequest(content);
                        }
                    } else {
                        throw new VideoGenerateException("content fail.");
                    }
                //--------------------------------------------
                //- 
                //--------------------------------------------
                case COMPLETED:
                    return this.stepCompleted(content);
                //--------------------------------------------
                //- 
                //--------------------------------------------
                default:
                    throw new VideoGenerateException(String.format("content video status error. status=%s", content.getVideoStatus()));
            }
        } catch(VideoGenerateException e){
            log.error("next(): error.", e);
            content.setVideoFail("생성 오류");
            this.contentRepository.save(content);
        } catch (InterruptedException e) {
            log.info("next(): InterruptedException.", e);
            content.setVideoFail("작업 중단");
            this.contentRepository.save(content);
            Thread.currentThread().interrupt();
        } catch(Exception e){
            log.error("next(): Exception.", e);
            content.setVideoFail("Exception");
            this.contentRepository.save(content);
        } 
        return 0;
    }


    private CreateRenderRequest createCreateRenderRequest(Content content){
        var items = content.getItems();
        var itemsSize = items.size();

        CreateRenderRequest request = CreateRenderRequest.createVideo();
        request.setMetadata(String.valueOf(content.getId()));
        request.setMaxWidth(720);
        request.setMaxWidth(1280);
        request.setFrameRate(60);
        request.addModification("title_text", content.getTitle());
        request.addModification("quotation_theme", content.getSubject());
        request.addModification("quotation_count", String.valueOf(itemsSize));
        for(int i=0 ; i < itemsSize; i++){
            var item = items.get(i);
            String number = String.valueOf(i+1);

            // text : quotation|speaker_name|gender|quotation_source
            var text = item.getText().split("\\|");
            var textEng = item.getTextEng().split("\\|");
            request.addModification("quotation_"+number, text.length > 0 ? text[0] : "");
            request.addModification("speaker_name_"+number, text.length > 1 ? text[1] : "");
            request.addModification("quotation_source_"+number, text.length > 3 ? text[3] : "");

            request.addModification("quotation_"+number+"_eng", textEng.length > 0 ? textEng[0] : "");
            request.addModification("speaker_name_"+number+"_eng", textEng.length > 1 ? textEng[1] : "");
            request.addModification("quotation_source_"+number+"_eng", textEng.length > 3 ? textEng[3] : "");

            request.addModification("speaker_voice_"+number, item.getVoiceUrl());
            request.addModification("bg_image_"+number, item.getImageUrl());
        }
        return request;
    }

    /**
     * 비디오 생성 요청
     * @param content
     * @return
     * @throws InterruptedException 
     */
    @SuppressWarnings("squid:S1301")
    private int stepRequest(Content content){
        log.info("stepRequest(): start. id={}", content.getId());

        var renderid = content.getVideoTxId();
        if(!StringUtils.hasText(renderid)){
            CreatomateProjectTemplate projectTemplate = this.creatomateGenerator.getActiveProjectTemplate();

            var request = this.createCreateRenderRequest(content);
            request.setTemplateId(projectTemplate.getTemplateId());
            
            CommonResponse<RenderResponse> response = creatomateApi.createRenderThenFirst(projectTemplate.getApiKey(), request);
            if(!response.isSucceed()){
                switch (response.getCode()) {
                    case 402:
                        this.creatomateGenerator.disableProject(projectTemplate.getId(), String.format("error. code=%s", response.getCode()));
                        return 0;
                    default:
                        throw new VideoGenerateException(String.format("creatomateClient.createRenderThenFirst(): error. status=%s", response.getCode()));
                }
            }

            var renderResponse = response.getData();

            content.setVideoProcess(renderResponse.getId());
            this.contentRepository.save(content);

            this.creatomateGenerator.addRender(projectTemplate.getId(), renderResponse);
            return 30;
           
        } else {
            content.setVideoProcess(renderid);
            this.contentRepository.save(content);
        }
        return 0;
    }

    /**
     * 비디오 생성중
     * @param contentItem
     * @return
     * @throws InterruptedException 
     */
    @SuppressWarnings("squid:S3776")
    private int stepProcess(Content content) throws InterruptedException {
        log.info("stepProcess(): start. id={}", content.getId());

        var renderId = content.getVideoTxId();
        if(!StringUtils.hasText(renderId)){
            content.resetVideoReady();
            this.contentRepository.save(content);
            return 0;
        }

        var render = this.creatomateGenerator.getRender(renderId)
            .orElseThrow(()->new VideoGenerateException(String.format("render data notfound. content_id={}, render_id=%s", content.getId(), renderId)));
        switch (render.getStatus()) {
            case RenderStatus.SUCCEEDED:
                content.setVideoCompleted(render.getId(), render.getUrl(), render.getSnapshotUrl());
                this.contentRepository.save(content);
                return 0;
            case RenderStatus.FAILED:
                content.setVideoFail("render 조회 오류");
                this.contentRepository.save(content);
                return 0;
            default:
                break;
        }

        CreatomateProject project = this.creatomateGenerator.getProject(render.getProjectId())
            .orElseThrow(()->new VideoGenerateException(String.format("creatomate project data notfound. content_id={}, project_id=%s", content.getId(), render.getProjectId())));

        var getRenderResponse = this.creatomateApi.getRender(project.getApiKey(), renderId);
        if(!getRenderResponse.isSucceed()){
            throw new VideoGenerateException(String.format("creatomateClient.getRender('%s'): error. status=%s", renderId, getRenderResponse.getCode()));
        }

        var renderResponse = getRenderResponse.getData();
        if(!renderResponse.getStatus().equals(render.getStatus())){
            render.updateStatus(renderResponse.getStatus(), renderResponse.getErrorMessage());
            this.creatomateGenerator.save(render);
        }
        
        switch (renderResponse.getStatus()) {
            case RenderStatus.PLANNED:
            case RenderStatus.WAITING:
            case RenderStatus.TRANSCRIBING:
            case RenderStatus.RENDERING:
                break;
            case RenderStatus.SUCCEEDED:
                content.setVideoCompleted(renderResponse.getId(), renderResponse.getUrl(), renderResponse.getSnapshotUrl());
                this.contentRepository.save(content);
                return 0;
            case RenderStatus.FAILED:
                content.setVideoFail("creatomate 생성 실패");
                this.contentRepository.save(content);
                break;
            default:
                break;
        }

        return 30;
    }

    /**
     * 비디오 생성 완료
     * @param contentItem
     * @return
     */
    private int stepCompleted(Content content) {
        log.info("stepCompleted(): start. id={}", content.getId());

        if(!content.isVideoUrl()){
            if(content.isVideoTxId()){
                content.setVideoProcess(content.getVideoTxId());
            } else {
                content.resetVideoReady();
            }
            this.contentRepository.save(content);
        }
        return 0;
    }

    
}
