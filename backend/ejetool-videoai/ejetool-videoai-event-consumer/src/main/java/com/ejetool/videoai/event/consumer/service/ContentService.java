package com.ejetool.videoai.event.consumer.service;

import com.ejetool.videoai.event.consumer.dto.content.GenerateItemImageParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemImageResult;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceResult;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoResult;
import com.ejetool.videoai.event.consumer.dto.content.PublishContentParam;
import com.ejetool.videoai.event.consumer.dto.content.PublishContentResult;

public interface ContentService {

    GenerateItemImageResult generateItemText(GenerateItemImageParam param);

    GenerateItemImageResult generateItemImage(GenerateItemImageParam param);

    GenerateItemVoiceResult generateItemVoice(GenerateItemVoiceParam param);

    GenerateVideoResult generateVideo(GenerateVideoParam param);

    PublishContentResult publishVideo(PublishContentParam param);

}