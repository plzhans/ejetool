package com.ejetool.videoai.service.application;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.ejetool.common.exception.BadRequestException;
import com.ejetool.common.logger.annotation.Loggable;
import com.ejetool.lib.telegram.TelegramPollinBot;
import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.videoai.common.content.ContentItemStatus;
import com.ejetool.videoai.common.content.ContentItemType;
import com.ejetool.videoai.common.content.ContentStatus;
import com.ejetool.videoai.common.content.ContentType;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.domain.entity.ContentItem;
import com.ejetool.videoai.domain.repository.ContentItemRepository;
import com.ejetool.videoai.domain.repository.ContentRepository;
import com.ejetool.videoai.event.publisher.publisher.VideoAIEventPublisher;
import com.ejetool.videoai.service.command.GptQueryCommand;
import com.ejetool.videoai.service.dto.content.*;
import com.ejetool.videoai.service.dto.prompt.PromptContentResult;
import com.ejetool.videoai.service.mapper.ContentDomainMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    
    private final ContentRepository repository;
    private final ContentItemRepository itemRepository;
    private final ContentDomainMapper domainMapper;
    private final TelegramPollinBot telegramBot;
    private final GptQueryCommand gptQueryCommand;
    private final VideoAIEventPublisher publisher;

    @Override
    public ContentCreateResult create(ContentCreateParam param) {

        var content = Content.builder()
            .title(param.getTitle())
            .build();

        var newContent = this.repository.save(content);

        var result = ContentCreateResult.builder()
            .contentId(newContent.getId())
            .build();
        
        return result;
    }

    @Loggable
    @Override
    public ContentCreateResult createByAI(ContentCreateByAIParam param) {

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(this.telegramBot.getChatIdOrDefault(param.getTelegramChatId()))
            .messageId(param.getTelegramMessageId())
            .build();
        telegramNotify.emptyThenSend(()->String.format("*컨텐츠 생성 요청*\n>> 타입: %s\n>> 주제 : %s", 
            param.getContentType().getDesc(), param.getSubject()));
        
        long contentId = 0;
        telegramNotify.send("Content generating...");
        try {
            if(param.getContentType() != ContentType.QUOTE) {
                throw new BadRequestException("invalid content_type");
            }
            
            var newContent = Content.builder()
                .contentType(ContentType.QUOTE)
                .subject(param.getSubject())
                .title(String.format("%s에 관한 명언", param.getSubject()))
                .build();
            newContent.setTelegramMessage(telegramNotify.getChatId(), telegramNotify.getMessageId());
           
            Content content = this.repository.save(newContent);
            contentId = content.getId();

            publisher.contentCreated(content.getId());
            
            telegramNotify.setPrefixText(content.getTelegramPrefixText());
            telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_generate_item_text", contentId));
            
            telegramNotify.send("컨텐츠 내용 생성중...(by GPT)");
            
            content.setItemTextReqeust(param.getGptModel());
            this.generateItemText(param.getRequestId(), content, param.isCacheDisabled());
    
            telegramNotify.setPrefixText(content.getTelegramPrefixText());
            telegramNotify.send("컨텐츠 내용 생성 완료.", this.replyMarkupItemTextConfirm(content.getId()));
         
            if(content.isItemTextConfirmAgree()){
                publisher.contentItemTextCreated(content.getId());
                if(content.isGenerateItemImage()){
                    telegramNotify.send("이미지 생성 요청...");
                    publisher.contentItemImageGenerateRequest(content.getId(), false, false);
                } else if(content.isGenerateItemVoice()){
                    telegramNotify.send("음성 생성 요청...");
                    publisher.contentItemVoiceGenerateRequest(content.getId(), false, false);
                }
            } 
    
            var result = ContentCreateResult.builder()
                .contentId(content.getId())
                .status(content.getStatus())
                .build();
            return result;
        } catch(Exception e){
            if(telegramNotify.getDefaultReplyKeyboard() == null){
                telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_generate_item_text", contentId));
            }
            telegramNotify.send("Content generating error.");
            throw e;
        }
    }

    private void generateItemText(String requestId, Content content, boolean cacheDisabled){
        PromptContentResult promptContentResult = this.gptQueryCommand.createQuotes(requestId, content.getSubject(), content.getItemTextGptModel(), cacheDisabled);

        var contentItems = promptContentResult.getItems().stream()
            .map(quoteItem->{
                var contentItem = ContentItem.createNew(content);
                contentItem.setTextCompleted(quoteItem.getText().toJoinString(), quoteItem.getTextEn().toJoinString(), quoteItem.getTextKeyword(), quoteItem.getTextEngKeyword(), quoteItem.getImagePrompt());
                return contentItem;
            }).toList();
    
        
        content.setItemTextCreate(promptContentResult.getTextEng(), contentItems, true);
        this.repository.save(content);
    }

    private InlineKeyboardMarkup replyMarkupRemoveAndRetry(String target, long contentId, Object... args){
        if(contentId == 0 ){
            return null;
        }

        String retryArgs = Arrays.stream(args)
            .map(Object::toString)
            .collect(Collectors.joining(" "));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Arrays.asList(
            Arrays.asList(
                InlineKeyboardButton.builder()
                    .text("Remove")
                    .callbackData(String.format("/content_remove %s", contentId))
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Retry")
                    .callbackData(String.format("/content_%s %s %s", target, contentId, retryArgs))
                    .build()
            )
        ));
        return markup;
    }

    private InlineKeyboardMarkup replyMarkupItemTextConfirm(long contentId){
        if(contentId == 0 ){
            return null;
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Arrays.asList(
            Arrays.asList(
                InlineKeyboardButton.builder()
                    .text("Remove")
                    .callbackData(String.format("/content_remove %s", contentId))
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Regenerate")
                    .callbackData(String.format("/content_agree_item_text %s %s", contentId, false))
                    .build()
            ),
            Arrays.asList(
                InlineKeyboardButton.builder()
                    .text("Agree")
                    .callbackData(String.format("/content_agree_item_text %s", contentId))
                    .build()
            )
        ));
        return markup;
    }

    @Override
    public AgreeContentItemResult agreeItemText(AgreeContentItemParam param) {
        Content content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_agree_item_text", param.getContentId(), param.isAgree()));
        telegramNotify.setPrefixText(content.getTelegramPrefixText());

        try {
            if(content.isItemTextConfirmAgree() != param.isAgree()){
                content.setItemConfirm(ContentItemType.TEXT, param.isAgree());
                this.repository.save(content);
            }

            if(content.getStatus() != ContentStatus.READY
                && content.getStatus() != ContentStatus.REQUEST){
                throw new BadRequestException("Invalid content status. status="+content.getStatus());
            }

            if(content.isItemTextConfirmAgree()){
                publisher.contentItemTextCreated(content.getId());
                if(content.isGenerateItemImage()){
                    telegramNotify.send("이미지 생성 요청...");
                    publisher.contentItemImageGenerateRequest(content.getId(), false, false);
                } else if(content.isGenerateItemVoice()){
                    telegramNotify.send("음성 생성 요청...");
                    publisher.contentItemVoiceGenerateRequest(content.getId(), false, false);
                }
            } else {
                content.setItemTextReady();
                this.repository.save(content);

                telegramNotify.setPrefixText(content.getTelegramPrefixText());
                telegramNotify.send("컨텐츠 내용 재생성중...(by GPT)");
                this.generateItemText(param.getRequestId(), content, true);

                telegramNotify.setPrefixText(content.getTelegramPrefixText());
                telegramNotify.send("컨텐츠 내용 생성 완료.", this.replyMarkupItemTextConfirm(content.getId()));
            }

            return new AgreeContentItemResult(content.getItemTextStatus());
        } catch(Exception e){
            telegramNotify.send("Content item text agree error.");
            throw e;
        }
    }

    @Override
    public AgreeContentItemResult agreeItemImage(AgreeContentItemParam param) {
        Content content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_agree_item_image", param.getContentId()));

        try {
            if(content.isItemImageConfirmAgree() != param.isAgree()){
                content.setItemConfirm(ContentItemType.IMAGE, param.isAgree());
                this.repository.save(content);
            }

            if(content.isGenerateItemImage()){
                publisher.contentItemImageCreated(content.getId());
                if(content.isGenerateItemVoice()){
                    telegramNotify.send("음성 생성 요청...");
                    publisher.contentItemVoiceGenerateRequest(content.getId(), false, false);
                }
            } 
            return new AgreeContentItemResult(content.getItemImageStatus());
        } catch(Exception e){
            telegramNotify.send("Content item image agree error.");
            throw e;
        }
    }

    @Override
    public AgreeContentItemResult agreeItemVoice(AgreeContentItemParam param) {
        Content content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(content.getTelegramChatId())
            .messageId(content.getTelegramMessageId())
            .build();
        telegramNotify.setDefaultReplyKeyboard(this.replyMarkupRemoveAndRetry("content_agree_item_voice", param.getContentId()));

        try {
            if(content.isItemVoiceConfirmAgree() != param.isAgree()){
                content.setItemConfirm(ContentItemType.VOICE, param.isAgree());
                this.repository.save(content);
            }

            if(content.isItemVoiceConfirmAgree()){
                publisher.contentItemVoiceCreated(content.getId());
                if(content.isAutoGenerateVideo()){
                    telegramNotify.send("비디오 생성 요청...");
                    publisher.contentVideoGenerateRequest(content.getId(), false, false);
                }
            }
            return new AgreeContentItemResult(content.getItemImageStatus());
        } catch(Exception e){
            telegramNotify.send("Content item voice agree error.");
            throw e;
        }
    }

    @Override
    public Optional<ContentDto> get(long contentId) {
        return this.repository.findById(contentId)
            .map(p->domainMapper.from(p));
    }

    @Override
    public Optional<ContentDto> update(long contentId, ContentUpdateParam param) {
        var content =this.repository.findById(contentId)
            .orElseThrow(NoSuchElementException::new);
        
        var dto = domainMapper.from(content);
        return Optional.of(dto);
    }

    @Override
    public boolean delete(long id) {
        this.repository.deleteById(id);
        return true;
    }

    @Override
    public ContentItemGenerateResult generateItemText(ContentItemGenerateParam param) {
        var content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        publisher.contentItemTextGenerateRequest(param.getContentId(), param.isFailRetry(), false);
        return new ContentItemGenerateResult(content.getStatus());
    }


    @Override
    public ContentItemGenerateResult generateItemImage(ContentItemGenerateParam param) {
        var content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        publisher.contentItemImageGenerateRequest(param.getContentId(), param.isFailRetry(), false);
        return new ContentItemGenerateResult(content.getStatus());
    }

    @Override
    public ContentItemGenerateResult generateItemVoice(ContentItemGenerateParam param) {
        var content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        publisher.contentItemVoiceGenerateRequest(param.getContentId(), param.isFailRetry(), false);
        return new ContentItemGenerateResult(content.getStatus());
    }

    @Override
    public ContentItemGenerateResult generateVideo(ContentItemGenerateParam param) {
        var content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        publisher.contentVideoGenerateRequest(param.getContentId(), param.isFailRetry(), false);
        return new ContentItemGenerateResult(content.getStatus());
    }

    @Override
    public ContentPublishVideoResult publishVideo(ContentPublishVideoParam param) {
        var content = this.repository.findById(param.getContentId())
            .orElseThrow(NoSuchElementException::new);

        publisher.contentPublishRequest(param.getContentId(), param.isFailRetry(), false);
        return new ContentPublishVideoResult(content.getPublishStatus());
    }


    /**
     * 아이템 텍스트 동의 여부
     */
    @Override
    public ContentItemConfirmResult setItemTextConfirm(ContentItemConfirmParam param) {
        return null;
    }
    
    @Override
    public ContentItemConfirmResult setItemImageConfirm(ContentItemConfirmParam param) {
        return null;
    }

    @Override
    public ContentItemConfirmResult setItemVoiceConfirm(ContentItemConfirmParam param) {
        return null;
    }

    @Override
    public ContentConfirmResult setVideoConfirm(ContentConfirmParam param) {
        return null;
    }

    @Transactional
    @Override
    public UpdateContentItemResult updateItem(UpdateContentItemParam param) {
        TelegramChatNotify telegramNotify = TelegramChatNotify.builder()
            .telegramBot(this.telegramBot)
            .chatId(this.telegramBot.getChatIdOrDefault(param.getTelegramChatId()))
            .messageId(param.getTelegramMessageId())
            .build();
        
        telegramNotify.send("Content updating...");
        try {
            var contentItem =this.itemRepository.findById(param.getItemId())
                .orElseThrow(NoSuchElementException::new);
            if(param.getContentId() != null && param.getContentId() != contentItem.getContent().getId()){
                throw new NoSuchElementException();
            }

            if(contentItem.getContent().getItemImageStatus() == ContentItemStatus.COMPLETED){
                throw new BadRequestException("Content completed.");
            }

            if(param.getImagePrompt() != null){
                contentItem.setImageImage(param.getImagePrompt(), true);
            }
    
            var result = UpdateContentItemResult.builder()
                .textStatus(contentItem.getTextStatus())
                .imageStatus(contentItem.getImageStatus())
                .voiceStatus(contentItem.getVoiceStatus())
                .build();
            return result;
        } catch(Exception e){
            telegramNotify.send("Content item update error.");
            throw e;
        }
    }
}
