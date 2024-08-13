package com.ejetool.videoai.domain.entity;

import java.time.OffsetDateTime;

import com.ejetool.common.db.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "creatomate_project", 
    indexes = {
    }
)
public class CreatomateProject extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int id;

    /**
     * owner id
     */
    @Column(name = "owner_id")
    private String ownerId;

    /**
     * 활성화 여부
     */
    @Column(name = "enabled")
    private boolean enabled = false;

    /**
     * 비활성화 날짜
     */
    @Column(name = "dislabed_date")
    private OffsetDateTime dislabedDate;

    /**
     * 비활성화 사유
     */
    @Column(name = "disabled_cause")
    private String disabledCause;

    /**
     * api key
     */
    @Lob
    @Column(name = "api_key", columnDefinition = "TEXT")
    private String apiKey;

    /**
     * 기본 template_id
     */
    @Column(name = "template_id")
    private String templateId;

    /**
     * 
     * @param accountId
     * @param apiKey
     * @param templateId
     */
    public CreatomateProject(String accountId, String apiKey, String templateId){
        this.ownerId = accountId;
        this.apiKey = apiKey;
        this.templateId = templateId;
    }

    public void setDisabled(String disabledCause){
        this.enabled = false;
        this.dislabedDate = OffsetDateTime.now();
        this.disabledCause = disabledCause;
    }
}
