package com.ejetool.videoai.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ejetool.videoai.domain.entity.OpenaiPrompt;

///
public interface OpenaiRepository extends JpaRepository<OpenaiPrompt, Long> {
    Optional<OpenaiPrompt> findByName(String name);
}
