package com.ejetool.videoai.event.consumer.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.ejetool.lib.telegram.TelegramBotSender;
import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.lib.telegram.util.TelegramUtils;
import com.ejetool.lib.youtube.consts.YoutubeVideoLicense;
import com.ejetool.lib.youtube.consts.YoutubeVideoPrivacyStatus;
import com.ejetool.lib.youtube.consts.YoutubeVideoUploadCategory;
import com.ejetool.lib.youtube.dto.CreateYoutubeVideoParam;
import com.ejetool.lib.youtube.dto.YoutubeVideoSnippet;
import com.ejetool.lib.youtube.dto.YoutubeVideoStatus;
import com.ejetool.lib.youtube.service.YoutubeService;
import com.ejetool.videoai.common.content.ContentItemStatus;
import com.ejetool.videoai.common.content.ContentPublishStatus;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.domain.entity.ContentItem;
import com.ejetool.videoai.domain.repository.ContentRepository;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemImageParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemImageResult;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceResult;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoResult;
import com.ejetool.videoai.event.consumer.dto.content.PublishContentParam;
import com.ejetool.videoai.event.consumer.dto.content.PublishContentResult;
import com.ejetool.videoai.event.consumer.exception.PublishGenerateException;
import com.ejetool.videoai.event.consumer.exception.VideoGenerateException;
import com.ejetool.videoai.event.consumer.processor.interfaces.ImageGenerateProcessor;
import com.ejetool.videoai.event.consumer.processor.interfaces.VideoGenerateProcessor;
import com.ejetool.videoai.event.consumer.processor.interfaces.VoiceGenerateProcessor;
import com.ejetool.videoai.event.publisher.publisher.VideoAIEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository repository;
    private final VideoAIEventPublisher publisher;
    private final ImageGenerateProcessor imageGenerateProcessor;
    private final VoiceGenerateProcessor voiceGenerateProcessor;
    private final VideoGenerateProcessor videoGenerateProcessor;
    private final YoutubeService youtubeService;
    private final TelegramBotSender telegramBot;

    private InlineKeyboardMarkup replyMarkupRemoveAndRetry(String target, long contentId){
        if(contentId == 0 ){
            return null;
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Arrays.asList(
            Arrays.asList(
                InlineKeyboardButton.builder()
                    .text("Remove")
                    .callbackData(String.format("/content_delete %s", contentId))
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Retry")
                    .callbackData(String.format("/%s %s", target, contentId))
                    .build()
            )
        ));
        return markup;
    }

    /**
     * 텍스트 생성
     */
    @Override
    public GenerateItemImageResult generateItemText(GenerateItemImageParam param) {
        var content = this.repository.findByIdWithContentItems(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setPrefixText(content.getTelegramPrefixText());
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_generate_item_text", content.getId()));

        telegramNotify.send("텍스트 생성중...");
        try {
            return new GenerateItemImageResult();
        } catch(Exception e){
            telegramNotify.send(String.format("Text generation error\n>>message: %s", e.getMessage()));
            throw e;
        }
    }

    /**
     * 이미지 생성
     */
    @Override
    public GenerateItemImageResult generateItemImage(GenerateItemImageParam param) {
        var content = this.repository.findByIdWithContentItems(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setPrefixText(content.getTelegramPrefixText());
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_generate_item_image", content.getId()));

        telegramNotify.send("이미지 생성중...");
        try {
            if(content.getItemImageStatus() == ContentItemStatus.COMPLETED && !param.isForce()){
                this.generateItemImageCompleted(content, telegramNotify);
                return new GenerateItemImageResult();
            }
    
            this.imageGenerateProcessor.generate(content, param, telegramNotify);
            
            if(content.getItemImageStatus() == ContentItemStatus.COMPLETED){
                this.generateItemImageCompleted(content, telegramNotify);
            } else if(content.getItemImageStatus() == ContentItemStatus.MODERATED){
                telegramNotify.send(String.format("Image moderated.", content.getItemImageStatus()));
                content.getItems().stream()
                    .filter(x->x.getImageStatus() == ContentItemStatus.MODERATED)
                    .forEach(item->{
                        StringBuilder sbText = new StringBuilder();
                        sbText.append("*컨텐츠 아이템*\n");
                        sbText.append(">> item\\_id: ").append(item.getId()).append("\n");
                        sbText.append(">> prompt: ").append(TelegramUtils.toSafeText(item.getImagePrompt())).append("\n");
                        sbText.append("\n").append("Image moderated.");

                        var markup = InlineKeyboardMarkup.builder()
                            .keyboardRow(Arrays.asList(
                                InlineKeyboardButton.builder()
                                    .switchInlineQueryCurrentChat(String.format("/content_item_image_prompt %s %s", item.getId(), item.getImagePrompt()))
                                    .text("Prompt edit")
                                    .build()
                            ))
                            .build();
                        telegramNotify.sendReply(sbText.toString(), markup);
                    }
                );
            } else {
                telegramNotify.send(String.format("Image generation fail. status=%s", content.getItemImageStatus()));
            }
            return new GenerateItemImageResult();
        } catch(Exception e){
            telegramNotify.send(String.format("Image generation error\n>>message: %s", e.getMessage()));
            throw e;
        }
    }

    private void generateItemImageCompleted(Content content, TelegramChatNotify telegramNotify){
        telegramNotify.send("Image generation completed.");

        publisher.contentItemImageCreated(content.getId());
        if(content.isAutoGenerateVoice()){
            publisher.contentItemVoiceGenerateRequest(content.getId(), false, false);
        }
    }

    /**
     * 음성 생성
     */
    @Override
    public GenerateItemVoiceResult generateItemVoice(GenerateItemVoiceParam param) {
        var content = this.repository.findByIdWithContentItems(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setPrefixText(content.getTelegramPrefixText());
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_generate_item_voice", content.getId()));

        telegramNotify.send("음성 생성중...");
        try{
            if(content.getItemVoiceStatus() == ContentItemStatus.COMPLETED && param.isForce()){
                this.generateItemVoiceCompleted(content, telegramNotify);
                return new GenerateItemVoiceResult();
            }
    
            this.voiceGenerateProcessor.generate(content, param, telegramNotify);
    
            var contentItems = content.getItems();
            var createdCount = contentItems.stream()
                .filter(x->x.getVoiceStatus() == ContentItemStatus.COMPLETED)
                .count();
            if(createdCount == contentItems.size()){
                content.setItemVoiceCompleted();
            } else {
                content.setItemVoiceFail();
            }
            this.repository.save(content);
            
            if(content.getItemVoiceStatus() == ContentItemStatus.COMPLETED){
                this.generateItemVoiceCompleted(content, telegramNotify);
            }
            return new GenerateItemVoiceResult();
        } catch(Exception e){
            telegramNotify.send(String.format("Voice generation error!\n>>message: %s", e.getMessage()));
            throw e;
        }
    }

    private void generateItemVoiceCompleted(Content content, TelegramChatNotify telegramNotify){
        telegramNotify.send("음성 생성 완료.");

        publisher.contentItemVoiceCreated(content.getId());
        if(content.isAutoGenerateVideo()){
            publisher.contentVideoGenerateRequest(content.getId(), false, false);
        }
    }

    @Override
    public GenerateVideoResult generateVideo(GenerateVideoParam param) {
        var content = this.repository.findByIdWithContentItems(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setPrefixText(content.getTelegramPrefixText());
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_generate_video", content.getId()));
        
        telegramNotify.send("비디오 생성중...");
        try {
            if(content.getVideoStatus() == ContentItemStatus.COMPLETED){
                if(!param.isForce()){
                    this.generateVideoCompleted(content, telegramNotify);
                    return new GenerateVideoResult();
                } else {
                    content.resetVideoReady();
                    this.repository.save(content);
                }
            }
    
            for (ContentItem item : content.getItems()) {
                if(!item.isTextCompleted()){
                    content.setVideoFail("텍스트 오류. item_id="+item.getId());
                    this.repository.save(content);
                    throw new VideoGenerateException(String.format("item text not completed. content_id=%s", content.getId(), item.getId()));
                }
                if(!item.isImageCompleted()){
                    content.setVideoFail("이미지 오류. item_id="+item.getId());
                    this.repository.save(content);
                    throw new VideoGenerateException(String.format("item image not completed. content_id=%s", content.getId(), item.getId()));
                }
                if(!item.isVoiceCompleted()){
                    content.setVideoFail("보이스 오류. item_id="+item.getId());
                    this.repository.save(content);
                    throw new VideoGenerateException(String.format("item voice not completed. content_id=%s", content.getId(), item.getId()));
                }
            }
    
            this.videoGenerateProcessor.generate(content, param, telegramNotify);
            
            if(content.getVideoStatus() == ContentItemStatus.COMPLETED){
                this.generateVideoCompleted(content, telegramNotify);
            } else {
                telegramNotify.send("비디오 생성 실패\n>> message: "+ content.getVideoFailCause());
            }
            return new GenerateVideoResult();
        } catch(Exception e){
            telegramNotify.send(String.format("Generate video error!\n>>message: %s", e.getMessage()));
            throw e;
        }
    }

    private void generateVideoCompleted(Content content, TelegramChatNotify telegramNotify){
        telegramNotify.send(String.format("비디오 생성 완료.\n>> tx\\_id: %s\n\n%s", content.getVideoTxId(), TelegramUtils.toSafeText(content.getVideoUrl())));
        
        publisher.contentVideoCreated(content.getId());
        if(content.isAutoPublish()){
            publisher.contentPublishRequest(content.getId(), false, false);
        }
    }

    @Override
    public PublishContentResult publishVideo(PublishContentParam param) {
        var content = this.repository.findByIdWithContentItems(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setPrefixText(content.getTelegramPrefixText());
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_publish-video", content.getId()));
        
        telegramNotify.send("비디오 발행중...");
        try{
            if(content.getPublishStatus() == ContentPublishStatus.COMPLETED && !param.isForce()){
                this.publishContentCompleted(content);
                return new PublishContentResult();
            }

            if(!content.isVideoCompleted()){
                content.setPublishFail();
                this.repository.save(content);
                throw new PublishGenerateException(String.format("video not completed.", content.getId()));
            }

            telegramNotify.send("YouTube 발행중...");

            var youtubeInsert = this.createYoutubeVideo(content);
            var video = this.youtubeService.insert(youtubeInsert);

            content.setPublishYoutubeCompleted(video.getUrl());
            this.repository.save(content);

            telegramNotify.setPrefixText(content.getTelegramPrefixText());
            telegramNotify.send(String.format("YouTube 발행 완료.\n%s", TelegramUtils.toSafeText(content.getPublishYoutubeUrl())), null);
            
            if(content.getPublishStatus() == ContentPublishStatus.COMPLETED){
                this.publishContentCompleted(content);
            }
            return new PublishContentResult();
        } catch(Exception e){
            telegramNotify.send(String.format("Video publishing error!\n>>message: %s", e.getMessage()));
            throw e;
        }
    }

    public CreateYoutubeVideoParam createYoutubeVideo(Content content){
        String title = content.getSocialShortTitle();
        StringBuilder description = new StringBuilder();
        description.append(content.getTitle()).append("\n\n");
        
        for(var item : content.getItems()){
            var text = item.getText()
                .replace("남성|", "")
                .replace("여성|", "")
                .replace("|", " | ");
            description.append(text).append('\n');
            var textEng = item.getTextEng()
                .replace("Male|", "")
                .replace("Female|", "")
                .replace("|", " | ");
            description.append(textEng).append("\n\n");
        }
        
        description.append("------------------------------\n");

        // 한국어 태그 만들기
        description.append("#shorts");
        var korTags = content.getItems().stream()
            .map(ContentItem::getTextKeyword)
            .flatMap(x -> Arrays.stream(x.split(",")))
            .collect(Collectors.toCollection(HashSet::new))
            .stream()
            .map(x->x.replaceAll("[^가-힣a-zA-Z0-9]", ""))
            .collect(Collectors.toList());
        for (String tag : korTags) {
            description.append(" #").append(tag);
        }
        description.append("\n");

        // 영어 태그 만들기
        var engTags = content.getItems().stream()
            .map(ContentItem::getTextKeywordEng)
            .flatMap(x -> Arrays.stream(x.split(",")))
            .collect(Collectors.toCollection(HashSet::new))
            .stream()
            .map(x->x.replaceAll("[^가-힣a-zA-Z0-9]", ""))
            .collect(Collectors.toList());
        for (String tag : engTags) {
            description.append(" #").append(tag);
        }
        description.append("\n\n");
        description.append("------------------------------\n");
        description.append("created by ejegong AI");

        YoutubeVideoSnippet snippet = new YoutubeVideoSnippet();
        snippet.setTags(korTags);
        snippet.setCategoryId(YoutubeVideoUploadCategory.EDUCATION);
        snippet.setTitle(title);
        snippet.setDescription(description.toString());

        YoutubeVideoStatus status = new YoutubeVideoStatus();
        status.setLicense(YoutubeVideoLicense.YOUTUBE);
        status.setPrivacyStatus(YoutubeVideoPrivacyStatus.PUBLIC);
        status.setEmbeddable(true);
        status.setPublicStatsViewable(true);
        status.setMadeForKids(false);

        var param = CreateYoutubeVideoParam.builder()
            .sourceId(String.valueOf(content.getId()))
            .sourceUrl(content.getVideoUrl())
            .snapshotUrl(content.getVideoSnapshotUrl())
            .snippet(snippet)
            .status(status)
            .build();
        return param;
    }

    private void publishContentCompleted(Content content){
        publisher.contentPublishCompleted(content.getId());
    }
}
