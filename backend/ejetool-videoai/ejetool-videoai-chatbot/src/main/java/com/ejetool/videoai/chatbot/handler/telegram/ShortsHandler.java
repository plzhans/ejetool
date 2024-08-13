package com.ejetool.videoai.chatbot.handler.telegram;

import java.util.Arrays;

import com.ejetool.videoai.client.EjetoolVideoAIClient;
import com.ejetool.videoai.client.base.RestResponse;
import com.ejetool.videoai.client.content.dto.ContentType;
import com.ejetool.videoai.client.content.dto.CreateContentByAIRequest;
import com.ejetool.videoai.client.content.dto.CreateContentResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.ejetool.lib.telegram.handler.CommandHandler;
import com.ejetool.lib.telegram.handler.CommandMapping;
import com.ejetool.lib.telegram.handler.CommandRequest;
import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.lib.telegram.util.TelegramUtils;

@Slf4j
@CommandHandler
@RequiredArgsConstructor
@Component @Scope("prototype")
public class ShortsHandler {

    private final EjetoolVideoAIClient ejetoolVideoAIClient;

    /**
     * 명언 생성
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping(value="/shorts_quote", roles="execute",  description = "Shorts 명언 생성")
    public void quote(CommandRequest request) throws TelegramApiException, InterruptedException{

        if(!request.isCommandArg()){
            request.sendReplyMessage(String.format("주제를 입력하세요. 예: %s {주제}", request.getCommandName()));
            return;
        }

        var replyMsg = SendMessage.builder()
            .chatId(request.getChatId())
            .parseMode(ParseMode.MARKDOWN)
            .text("ejetool 요청중...")
            .replyToMessageId(request.getMessageId())
            .build();
        TelegramChatNotify replyNotify = TelegramChatNotify.createAndSend(request.getSender(), replyMsg);

        CreateContentByAIRequest videoRequest = new CreateContentByAIRequest();
        videoRequest.setContentType(ContentType.QUOTE);
        videoRequest.setSubject(request.getCommandArg());
        videoRequest.setTelegramChatId(replyNotify.getChatId());
        videoRequest.setTelegramMsgId(replyNotify.getMessageId());

        try{
            RestResponse<CreateContentResponse> videoResponse = ejetoolVideoAIClient.content().createByAI(videoRequest);
            if(!videoResponse.isSucceed()){
                log.error("createQuote(): error. subject={}", videoRequest.getSubject());
                replyNotify.send(TelegramUtils.toSafeText("Error.\nmessage: %s", videoResponse.getMessage()));
            }
        } catch(Exception e){
            log.error(String.format("createQuote(): error. command={}, args={}", request.getCommandName(), request.getCommandArg()), e);
            
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(Arrays.asList(
                Arrays.asList(
                    InlineKeyboardButton.builder()
                        .text("Delete")
                        .callbackData(String.format("/message_delete %s %s %s", replyNotify.getChatId(), replyNotify.getMessageId(), replyMsg.getReplyToMessageId()))
                        .build(),
                    InlineKeyboardButton.builder()
                        .text("Retry")
                        .callbackData(request.getMessageText())
                        .build()
                )
            ));
            replyNotify.send(String.format("Error.\nmessage: %s", e.getMessage()), markup);
        }
    }
}
