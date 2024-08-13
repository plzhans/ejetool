package com.ejetool.videoai.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejetool.videoai.domain.entity.OpenaiUseHistory;

///
public interface OpenaiHistoryRepository extends JpaRepository<OpenaiUseHistory, Long> {
    
}
