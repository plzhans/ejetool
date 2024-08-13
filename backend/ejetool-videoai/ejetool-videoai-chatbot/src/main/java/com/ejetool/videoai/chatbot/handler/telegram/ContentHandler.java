package com.ejetool.videoai.chatbot.handler.telegram;

import com.ejetool.videoai.client.EjetoolVideoAIClient;
import com.ejetool.videoai.client.base.RestResponse;
import com.ejetool.videoai.client.content.dto.ContentPublishVideoRequest;
import com.ejetool.videoai.client.content.dto.AgreeContentItemResponse;
import com.ejetool.videoai.client.content.dto.UpdateContentItemRequest;
import com.ejetool.videoai.client.content.dto.UpdateContentItemResponse;
import com.ejetool.videoai.client.content.dto.ContentGenerateItemRequest;
import com.ejetool.videoai.client.content.dto.ContentGenerateVideoRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.ejetool.common.exception.BadRequestException;
import com.ejetool.lib.telegram.handler.CommandHandler;
import com.ejetool.lib.telegram.handler.CommandMapping;
import com.ejetool.lib.telegram.handler.CommandRequest;
import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.lib.telegram.util.TelegramUtils;

@Slf4j
@CommandHandler
@RequiredArgsConstructor
@Component @Scope("prototype")
@SuppressWarnings("squid:S1192")
public class ContentHandler {

    private final EjetoolVideoAIClient ejetoolVideoAIClient;

    /**
     * 
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping(value="/content_generate_item_text", roles="execute")
    public void generateItemText(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs();
        long contentId = Long.parseLong(args[0]);

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        ContentGenerateItemRequest apiRequest = new ContentGenerateItemRequest();
        apiRequest.setContentId(contentId);
        apiRequest.setFailRetry(true);

        try{
            RestResponse<String> apiResponse = ejetoolVideoAIClient.content().generateItemText(apiRequest);
            if(!apiResponse.isSucceed()){
                log.error("generateItemText(): error. content_id={}", apiRequest.getContentId());
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("generateItemText(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\nError generating item text.", true);
        }
    }

    /**
     * 
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping(value="/content_generate_item_image", roles="execute")
    public void generateItemImage(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs();
        long contentId = Long.parseLong(args[0]);

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        ContentGenerateItemRequest apiRequest = new ContentGenerateItemRequest();
        apiRequest.setContentId(contentId);
        apiRequest.setFailRetry(true);

        try{
            RestResponse<String> apiResponse = ejetoolVideoAIClient.content().generateItemImage(apiRequest);
            if(!apiResponse.isSucceed()){
                log.error("generateItemImageRequest(): error. content_id={}", apiRequest.getContentId());
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("generateItemImageRequest(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\nError generating item image.", true);
        }
    }

    /**
     * 
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping(value="/content_generate_item_voice", roles="execute")
    public void generateItemVoice(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs();
        long contentId = Long.parseLong(args[0]);

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        ContentGenerateItemRequest apiRequest = new ContentGenerateItemRequest();
        apiRequest.setContentId(contentId);
        apiRequest.setFailRetry(true);

        try{
            RestResponse<String> apiResponse = ejetoolVideoAIClient.content().generateItemVoice(apiRequest);
            if(!apiResponse.isSucceed()){
                log.error("generateItemVoice(): error. content_id={}", apiRequest.getContentId());
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("generateItemVoice(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: Error generating item voice.", true);
        }
    }

    /**
     * 
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping(value="/content_generate_video", roles="execute")
    public void generateVideo(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs();
        long contentId = Long.parseLong(args[0]);

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        ContentGenerateVideoRequest apiRequest = new ContentGenerateVideoRequest();
        apiRequest.setContentId(contentId);
        apiRequest.setFailRetry(true);

        try{
            RestResponse<String> apiResponse = ejetoolVideoAIClient.content().generateVideo(apiRequest);
            if(!apiResponse.isSucceed()){
                log.error("generateVideo(): error. content_id={}", apiRequest.getContentId());
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("generateVideo(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: Error generating video.", true);
        }
    }

    /**
     * 
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping(value="/content_publish_video", roles="execute")
    public void publishVideo(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs();
        long contentId = Long.parseLong(args[0]);

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        ContentPublishVideoRequest apiRequest = new ContentPublishVideoRequest();
        apiRequest.setContentId(contentId);
        apiRequest.setFailRetry(true);

        try{
            RestResponse<String> apiResponse = ejetoolVideoAIClient.content().publishVideo(apiRequest);
            if(!apiResponse.isSucceed()){
                log.error("publishVideo(): error. content_id={}", apiRequest.getContentId());
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("publishVideo(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: publish video fail.", true);
        }
    }

    @CommandMapping(value="/content_agree_item_text", roles="execute")
    public void agreeItemText(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs(2);
        long itemId = Long.parseLong(args[0]);
        boolean agree = true;
        if(args.length>1 && StringUtils.hasText(args[1])){
            agree = Boolean.parseBoolean(args[1]);
        }

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        try{
            RestResponse<AgreeContentItemResponse> apiResponse = ejetoolVideoAIClient.content().agreeItemText(itemId, agree);
            if(!apiResponse.isSucceed()){
                log.error("setItemTextAgree(): error. item_id={}", itemId);
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("setItemTextAgree(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: update item fail.", true);
        }
    }

    @CommandMapping(value="/content_agree_item_image", roles="execute")
    public void agreeItemImage(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs(2);
        long itemId = Long.parseLong(args[0]);
        boolean agree = true;
        if(args.length>1 && StringUtils.hasText(args[1])){
            agree = Boolean.parseBoolean(args[1]);
        }

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        try{
            RestResponse<AgreeContentItemResponse> apiResponse = ejetoolVideoAIClient.content().agreeItemImage(itemId, agree);
            if(!apiResponse.isSucceed()){
                log.error("setItemImageAgree(): error. item_id={}", itemId);
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("setItemImageAgree(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: update item fail.", true);
        }
    }

    @CommandMapping(value="/content_agree_item_voice", roles="execute")
    public void agreeItemVoice(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs(2);
        long itemId = Long.parseLong(args[0]);
        boolean agree = true;
        if(args.length>1 && StringUtils.hasText(args[1])){
            agree = Boolean.parseBoolean(args[1]);
        }

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        try{
            RestResponse<AgreeContentItemResponse> apiResponse = ejetoolVideoAIClient.content().agreeItemVoice(itemId, agree);
            if(!apiResponse.isSucceed()){
                log.error("setItemVoiceAgree(): error. item_id={}", itemId);
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("setItemVoiceAgree(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: update item fail.", true);
        }
    }

    @CommandMapping(value="/content_item_image_prompt", roles="execute")
    public void updateImagePrompt(CommandRequest request) throws TelegramApiException, InterruptedException{
        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArgs(2);
        long itemId = Long.parseLong(args[0]);
        String imagePrompt = args[1];
        if(!StringUtils.hasText(imagePrompt)){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다. (prompt)");
            throw new BadRequestException("매개변수가 잘못되었습니다. (prompt)");
        }

        var replyMsg = request.createReplyMessage("ejetool 요청중...")
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg)
            .setCallbackQuery(request.getCallbackQueryId());

        UpdateContentItemRequest apiRequest = new UpdateContentItemRequest();
        apiRequest.setImagePrompt(imagePrompt);

        try{
            RestResponse<UpdateContentItemResponse> apiResponse = ejetoolVideoAIClient.content().updateItem(itemId, apiRequest);
            if(!apiResponse.isSucceed()){
                log.error("updateImagePrompt(): error. item_id={}", itemId);
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", apiResponse.getMessage()));
                return;
            }
            replyNotify.sendAnswerCallbackQuery("요청 완료");
            replyNotify.delete();
        } catch(Exception e){
            log.error(String.format("updateImagePrompt(): error. command={}", request.getCommandName()), e);
            replyNotify.sendAnswerCallbackQuery("*Error!*\n>> message: update item fail.", true);
        }
    }


    
}
