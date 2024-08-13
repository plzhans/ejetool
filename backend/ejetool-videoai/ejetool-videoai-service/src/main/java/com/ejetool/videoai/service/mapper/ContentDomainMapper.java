package com.ejetool.videoai.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ejetool.videoai.domain.entity.Content;
import com.ejetool.videoai.service.dto.content.ContentDto;

@Mapper(componentModel = "spring")
public interface ContentDomainMapper {
    /**
     * 
     * @param car input
     * @return output
     */
    @Mapping(target = "items", ignore = true)
    public ContentDto from(Content input);   
}
