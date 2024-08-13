package com.ejetool.videoai.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ejetool.videoai.domain.entity.Content;

///
public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("SELECT c FROM Content c JOIN FETCH c.items WHERE c.id = :content_id")
    Optional<Content> findByIdWithContentItems(@Param("content_id") long contentId);
}
