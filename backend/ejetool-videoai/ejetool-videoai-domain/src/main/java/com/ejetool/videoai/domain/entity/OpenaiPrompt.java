package com.ejetool.videoai.domain.entity;

import com.ejetool.common.db.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "openai_prompt",
    uniqueConstraints = {
        @UniqueConstraint(name = "idx_prompt_name", columnNames = "prompt_name")
    }
)
public class OpenaiPrompt extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prompt_id")
    private long id;

    @Column(name = "prompt_name", length = 100, unique = true)
    private String name;
    
    @Lob
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
}
