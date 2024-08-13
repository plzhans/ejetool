package com.ejetool.videoai.api.mapper;

import org.mapstruct.Mapper;

import com.ejetool.videoai.api.dto.quotes.*;
import com.ejetool.videoai.service.dto.quote.*;

@Mapper(componentModel = "spring")
public interface GptMapper {
    public CreateQuotesParam from(CreateQuotesRequest input);

    public CreateQuotesResponse from(CreateQuotesResult input);
}
