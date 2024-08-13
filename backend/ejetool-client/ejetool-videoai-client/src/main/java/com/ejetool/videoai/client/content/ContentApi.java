package com.ejetool.videoai.client.content;

import com.ejetool.videoai.client.base.RestResponse;
import com.ejetool.videoai.client.content.dto.ContentPublishVideoRequest;
import com.ejetool.videoai.client.content.dto.ContentResponse;
import com.ejetool.videoai.client.content.dto.CreateContentByAIRequest;
import com.ejetool.videoai.client.content.dto.CreateContentResponse;
import com.ejetool.videoai.client.content.dto.AgreeContentItemResponse;
import com.ejetool.videoai.client.content.dto.UpdateContentItemRequest;
import com.ejetool.videoai.client.content.dto.UpdateContentItemResponse;
import com.ejetool.videoai.client.content.dto.ContentGenerateItemRequest;
import com.ejetool.videoai.client.content.dto.ContentGenerateVideoRequest;

public interface ContentApi {

    RestResponse<CreateContentResponse> createByAI(CreateContentByAIRequest request);

    RestResponse<ContentResponse> getContent(long contentId);

    RestResponse<String> generateItemText(ContentGenerateItemRequest request);

    RestResponse<String> generateItemImage(ContentGenerateItemRequest request);

    RestResponse<String> generateItemVoice(ContentGenerateItemRequest request);

    RestResponse<String> generateVideo(ContentGenerateVideoRequest request);

    RestResponse<AgreeContentItemResponse> agreeItemText(long contentId, boolean agree);

    RestResponse<AgreeContentItemResponse> agreeItemImage(long contentId, boolean agree);

    RestResponse<AgreeContentItemResponse> agreeItemVoice(long contentId, boolean agree);

    RestResponse<String> publishVideo(ContentPublishVideoRequest request);

    RestResponse<UpdateContentItemResponse> updateItem(long itemId, UpdateContentItemRequest request);

    RestResponse<UpdateContentItemResponse> updateItem(long contentId, long itemid, UpdateContentItemRequest request);
}
