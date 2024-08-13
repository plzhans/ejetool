package com.ejetool.videoai.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejetool.videoai.domain.entity.ContentItem;

///
public interface ContentItemRepository extends JpaRepository<ContentItem, Long> {

}
