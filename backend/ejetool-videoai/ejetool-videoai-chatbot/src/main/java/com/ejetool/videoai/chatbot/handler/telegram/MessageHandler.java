package com.ejetool.videoai.chatbot.handler.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.ejetool.common.exception.BadRequestException;
import com.ejetool.lib.telegram.handler.CommandHandler;
import com.ejetool.lib.telegram.handler.CommandMapping;
import com.ejetool.lib.telegram.handler.CommandRequest;

@Slf4j
@CommandHandler
@RequiredArgsConstructor
@Component @Scope("prototype")
public class MessageHandler {

    /**
     * 명언 생성
     * @param request
     * @throws TelegramApiException 
     * @throws InterruptedException 
     */
    @CommandMapping("/message-delete")
    public void delete(CommandRequest request) throws TelegramApiException{

        if(!request.isCommandArg()){
            request.sendReplyMessage("Error.\nmessage: 매개변수가 잘못되었습니다.");
            throw new BadRequestException("매개변수가 잘못되었습니다.");
        }

        var args = request.getCommandArg().split(" ");
        if(args.length >= 2){
            var chatId = Long.valueOf(args[0]);
            var messageId = Integer.valueOf(args[1]);

            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId);

            request.getSender().execute(deleteMessage);

            if(args.length >= 3){
                messageId = Integer.valueOf(args[2]);

                deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(chatId);
                deleteMessage.setMessageId(messageId);

                request.getSender().execute(deleteMessage);
            }
        }
    }
}
