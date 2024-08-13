package com.ejetool.videoai.event.consumer.processor.interfaces;

import com.ejetool.lib.telegram.helper.TelegramChatNotify;
import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceParam;

public interface VoiceGenerateProcessor {
    
    boolean generate(Content content, GenerateItemVoiceParam param, TelegramChatNotify telegramNotify);
}
