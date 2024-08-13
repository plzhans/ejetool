package com.ejetool.videoai.domain.entity;

import org.springframework.util.StringUtils;

import com.ejetool.common.db.entity.BaseEntity;
import com.ejetool.videoai.common.content.ContentItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "content_item", 
    indexes = {
    }
)
public class ContentItem extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_item_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "content_id", foreignKey = @ForeignKey(name = "fk_content_item_content_id"))
    private Content content;

    /**
     * 텍스트 상태
     */
    @Column(name = "text_status")
    private ContentItemStatus textStatus = ContentItemStatus.NONE;

    /**
     * 텍스트
     */
    @Lob
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    /**
     * 텍스트
     */
    @Lob
    @Column(name = "text_eng", columnDefinition = "TEXT")
    private String textEng;

    /**
     * 텍스트 키워드
     */
    @Column(name = "text_keyword")
    private String textKeyword;

    /**
     * 텍스트 키워드 영어
     */
    @Column(name = "text_keyword_eng")
    private String textKeywordEng;

    /**
     * 텍스트
     */
    @Lob
    @Column(name = "image_prompt", columnDefinition = "TEXT")
    private String imagePrompt;

    /**
     * 이미지 상태
     */
    @Column(name = "image_status")
    private ContentItemStatus imageStatus = ContentItemStatus.NONE;

    /**
     * 이미지 생성 재시도
     */
    @Column(name = "image_retry_count")
    private int imageRetryCount = 0;

    /**
     * 이미지 tx id
     */
    @Column(name = "image_tx_id")
    private String imageTxId;

    /**
     * 이미지 url
     */
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    /**
     * 이미지 컨펌 여부
     */
    @Column(name = "image_confirm_agree")
    private boolean imageConfirmAgree;

    /**
     * 보이스 상태
     */
    @Column(name = "voice_status")
    private ContentItemStatus voiceStatus = ContentItemStatus.NONE;

    @Column(name = "voice_retry_count")
    private int voiceRetryCount = 0;

    /**
     * 보이스 url
     */
    @Column(name = "voice_url")
    private String voiceUrl;

    /**
     * 보이스 tx id
     */
    @Column(name = "voice_tx_id")
    private String voiceTxId;

    /**
     * 보이스 컨펌 여부
     */
    @Column(name = "voice_confirm_agree")
    private boolean voiceConfirmAgree;

    /** */
    public boolean isTextCompleted() {
        return this.textStatus == ContentItemStatus.COMPLETED
            && StringUtils.hasText(this.text)
            && StringUtils.hasText(this.textEng);
    }

    /** */
    public boolean isImageUrl(){
        return StringUtils.hasText(this.imageUrl);
    }

    /** */
    public boolean isImageTxId(){
        return StringUtils.hasText(this.imageTxId);
    }

    /** */
    public boolean isImageCompleted() {
        return this.imageStatus == ContentItemStatus.COMPLETED
            && this.imageConfirmAgree
            && StringUtils.hasText(this.imageTxId)
            && StringUtils.hasText(this.imageUrl);
    }

    /** */
    public boolean isImageFinish(boolean failRetry) {
        return this.imageStatus == ContentItemStatus.COMPLETED 
            || (!failRetry && this.imageStatus == ContentItemStatus.FAILED);
    }

    /** */
    public boolean isVoiceUrl(){
        return StringUtils.hasText(this.voiceUrl);
    }

    /** */
    public boolean isVoiceTxId(){
        return StringUtils.hasText(this.voiceTxId);
    }

    /** */
    public boolean isVoiceCompleted() {
        return this.voiceStatus == ContentItemStatus.COMPLETED
            && this.voiceConfirmAgree
            && StringUtils.hasText(this.voiceTxId)
            && StringUtils.hasText(this.voiceUrl);
    }

    /** */
    public boolean isVoiceFinish(boolean failRetry) {
        return this.voiceStatus == ContentItemStatus.COMPLETED 
            || (!failRetry && this.voiceStatus == ContentItemStatus.FAILED);
    }

    /** */
    public static ContentItem createNew(Content content){
        var entity = new ContentItem();
        entity.content = content;
        return entity;
    }

    /** */
    public void setTextCompleted(String text, String textEng, String textKeyword, String textKeywordEng, String imagePrompt){
        this.textStatus = ContentItemStatus.COMPLETED;
        this.text = text;
        this.textEng = textEng;
        this.textKeyword = textKeyword;
        this.textKeywordEng = textKeywordEng;
        this.imagePrompt = imagePrompt;
    }

    /** */
    public void resetImageReady() {
        this.imageTxId = null;
        this.imageUrl = null;
        this.imageConfirmAgree = false;
        this.imageStatus = ContentItemStatus.READY;
        this.imageRetryCount = 0;
    }

    /** */
    public void setImageProcess(String imageTxId) {
        this.imageTxId = imageTxId;
        this.imageStatus = ContentItemStatus.PROCESS;
    }

    /** */
    public void setImageProcessRetry(String imageTxId) {
        if(imageTxId.equals(this.imageTxId)){
            ++this.imageRetryCount;
        } else {
            this.imageTxId = imageTxId;
            this.imageRetryCount = 0;
        }
    }

    /** */
    public void setImageCompleted(String url) {
        this.imageUrl = url;
        this.imageConfirmAgree = true;
        this.imageStatus = ContentItemStatus.COMPLETED;
    }

    /** */
    public void setImageModerated() {
        this.imageStatus = ContentItemStatus.MODERATED;
    }

    public void setImageError() {
        this.imageStatus = ContentItemStatus.FAILED;
    }

    /** */
    public void resetVoiceReady() {
        this.voiceTxId = null;
        this.voiceUrl = null;
        this.voiceConfirmAgree = false;
        this.voiceStatus = ContentItemStatus.READY;
        this.voiceRetryCount = 0;
    }

    /** */
    public void setVoiceError() {
        this.voiceStatus = ContentItemStatus.FAILED;
    }
   
    /** */
    public void setVoiceCompleted(String voiceTxId, String url) {
        this.voiceTxId = voiceTxId;
        this.voiceUrl = url;
        this.voiceConfirmAgree = true;
        this.voiceStatus = ContentItemStatus.COMPLETED;
    }

    public void setImageImage(String imagePrompt, boolean reset) {
        this.imagePrompt = imagePrompt;
        if(reset){
            this.imageStatus = ContentItemStatus.READY;
            this.imageTxId = "";
            this.imageRetryCount = 0;
            this.imageUrl = "";
        }
        
    }
}
