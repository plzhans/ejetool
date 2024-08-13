package com.ejetool.videoai.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ejetool.videoai.api.dto.content.ContentResponse;
import com.ejetool.videoai.api.dto.content.CreateContentByAIRequest;
import com.ejetool.videoai.api.dto.content.CreateContentRequest;
import com.ejetool.videoai.api.dto.content.CreateContentResponse;
import com.ejetool.videoai.api.dto.content.AgreeContentItemResponse;
import com.ejetool.videoai.api.dto.content.UpdateContentItemRequest;
import com.ejetool.videoai.api.dto.content.UpdateContentItemResponse;
import com.ejetool.videoai.api.dto.content.ContentItemGenerateResponse;
import com.ejetool.videoai.api.dto.content.ContentPublishVideoResponse;
import com.ejetool.videoai.service.dto.content.*;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    public ContentCreateByAIParam to(CreateContentByAIRequest input);

    public ContentCreateParam to(CreateContentRequest input);

    public CreateContentResponse to(ContentCreateResult input);

    public ContentResponse to(ContentDto input);

    public ContentItemGenerateResponse to(ContentItemGenerateResult input);

    public ContentPublishVideoResponse to(ContentPublishVideoResult input);

    @Mapping(target = "itemId", source = "itemId")
    @Mapping(target = "contentId", source = "contentId")
    public UpdateContentItemParam to(UpdateContentItemRequest input, Long contentId, long itemId);

    public UpdateContentItemResponse to(UpdateContentItemResult input);

    public AgreeContentItemResponse to(AgreeContentItemResult result);
}
