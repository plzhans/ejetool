package com.ejetool.videoai.event.consumer.processor.interfaces;

import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoParam;

public interface VideoGenerateProcessor {
    
    boolean generate(Content content, GenerateVideoParam param, TelegramChatNotify telegramNotify);
}
