package com.ejetool.videoai.domain.entity;

import java.util.Map;

import com.ejetool.common.db.convert.MapToJsonConverter;
import com.ejetool.common.db.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "creatomate_render", 
    indexes = {
    }
)
@AllArgsConstructor
@Builder
public class CreatomateRender extends BaseEntity {
    /**
     * id
     */
    @Id
    @Column(name = "render_id")
    private String id;

    @Column(name = "project_id")
    private int projectId;

    @Column(name = "status")
    private String status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "snapshot_url", columnDefinition = "TEXT")
    private String snapshotUrl;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_tags")
    private String templateTags;
    
    @Column(name = "output_format")
    private String outputFormat;

    @Column(name = "render_scale")
    private int renderScale;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "frame_rate")
    private int frameRate;

    @Column(name = "duration")
    private float duration;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "webhook_url", columnDefinition = "TEXT")
    private String webhookUrl;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "modifications", columnDefinition = "MEDIUMTEXT")
    private Map<String, String> modifications;

    public void updateStatus(String status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
