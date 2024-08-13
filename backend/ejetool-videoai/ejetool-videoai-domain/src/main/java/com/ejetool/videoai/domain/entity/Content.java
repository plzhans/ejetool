package com.ejetool.videoai.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ejetool.common.db.entity.BaseEntity;
import com.ejetool.videoai.common.content.ContentItemStatus;
import com.ejetool.videoai.common.content.ContentItemType;
import com.ejetool.videoai.common.content.ContentPublishStatus;
import com.ejetool.videoai.common.content.ContentStatus;
import com.ejetool.videoai.common.content.ContentType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "content",
    indexes = {
        @Index(name = "idx_content_type", columnList = "content_type"),
    }
)
public class Content extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private long id;

    /**
     * 컨텐츠 타입
     */
    @Column(name = "content_type")
    private ContentType type = ContentType.NONE;

    /**
     * 제목
     */
    @Column(name = "title", length = 100)
    private String title;

    /**
     * 주제
     */
    @Column(name = "subject", length = 100)
    private String subject;

    /**
     * 주제
     */
    @Column(name = "subject_eng", length = 100)
    private String subjectEng;

    /**
     * 컨텐츠 상태
     */
    @Column(name = "content_status")
    private ContentStatus status = ContentStatus.NONE;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Column(name = "telegram_message_id")
    private Integer telegramMessageId;

    /**
     * 이미지 자동 생성 여부
     */
    @Column(name = "auto_generate_image")
    private boolean autoGenerateImage;

    /**
     * 음성 자동 생성 여부
     */
    @Column(name = "auto_generate_voice")
    private boolean autoGenerateVoice;

    /**
     * 음성 자동 생성 여부
     */
    @Column(name = "auto_generate_video")
    private boolean autoGenerateVideo;

    /**
     * 자동 발행 여부
     */
    @Column(name = "auto_publish")
    private boolean autoPublish;

    /**
     * 아이템 텍스트 상태
     */
    @Column(name = "item_text_status")
    private ContentItemStatus itemTextStatus = ContentItemStatus.NONE;

    /**
     * 아이템 텍스트 GPT Model
     */
    @Column(name = "item_text_gpt_model")
    private String itemTextGptModel;

    /**
     * 아이템 텍스트 컨펌 여부
     */
    @Column(name = "item_text_confirm_agree")
    private boolean itemTextConfirmAgree;

    /**
     * 아이템 텍스트 컨펌 동의 시간
     */
    @Column(name = "item_text_confirm_date")
    private OffsetDateTime itemTextConfirmDate;

    /**
     * 아이템 이미지 상태
     */
    @Column(name = "item_image_status")
    private ContentItemStatus itemImageStatus = ContentItemStatus.NONE;

    /**
     * 아이템 이미지 컨펌 동의 여부
     */
    @Column(name = "item_image_confirm_agree")
    private boolean itemImageConfirmAgree;

    /**
     * 아이템 이미지 컨펌 동의 시간
     */
    @Column(name = "item_image_confirm_date")
    private OffsetDateTime itemImageConfirmDate;

    /**
     * 아이템 음성 상태
     */
    @Column(name = "item_voice_status")
    private ContentItemStatus itemVoiceStatus = ContentItemStatus.NONE;

    /**
     * 아이템 음성 컨펌 여부
     */
    @Column(name = "item_voice_confirm_agree")
    private boolean itemVoiceConfirmAgree;

    /**
     * 아이템 음성 컨펌 동의 시간
     */
    @Column(name = "item_voice_confirm_date")
    private OffsetDateTime itemVoiceConfirmDate;

    /**
     * BGM 상태
     */
    @Column(name = "bgm_status")
    private ContentItemStatus bgmStatus = ContentItemStatus.NONE;

    /**
     * BGM 동의 여부
     */
    @Column(name = "bgm_confirm_agree")
    private boolean bgmConfirmAgree;

    /**
     * BGM 동의 시간
     */
    @Column(name = "bgm_confirm_date")
    private OffsetDateTime bgmConfirmDate;

    /**
     * 비디오 상태
     */
    @Column(name = "video_status")
    private ContentItemStatus videoStatus = ContentItemStatus.NONE;

    /**
     * 비디오 컨펌 동의 여부
     */
    @Column(name = "video_confirm_agree")
    private boolean videoConfirmAgree;

    /**
     * 비디오 컨펌 동의 시간
     */
    @Column(name = "video_confirm_date")
    private OffsetDateTime videoConfirmDate;

    /*
     * 비디오 생성 재시도
     */
    @Column(name = "video_retry_count")
    private int videoRetryCount = 0;

    /**
     * 비디오 실패 사유
     */
    @Column(name = "video_fail_cause", columnDefinition = "TEXT")
    private String videoFailCause;

    /**
     * 비디오 tx id
     */
    @Column(name = "video_tx_id")
    private String videoTxId;

   /**
    * 비디오 url
    */
   @Column(name = "video_url", columnDefinition = "TEXT")
   private String videoUrl;

   /**
    * 비디오 snapshot url
    */
   @Column(name = "video_snapshot_url", columnDefinition = "TEXT")
   private String videoSnapshotUrl;

    /**
     * 발행 상태
     */
    @Column(name = "publish_status")
    private ContentPublishStatus publishStatus = ContentPublishStatus.NONE;

    /**
     * 발행 날짜
     */
    @Column(name = "publish_date")
    private OffsetDateTime publishDate;

    /**
     * publish youtube url
     */
    @Column(name = "publish_youtube_url", columnDefinition = "TEXT")
    private String publishYoutubeUrl;

    /**
     * 컨텐츠 아이템
     */
    @OneToMany(mappedBy = "content", fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentItem> items = new ArrayList<>();
   
    @Builder
    public Content(ContentType contentType, String subject, String title) {
        this.type = contentType;
        this.subject = subject;
        this.title = title;
        this.status = ContentStatus.READY;
        this.autoPublish = true;
        this.autoGenerateImage = true;
        this.autoGenerateVoice = true;
        this.autoGenerateVideo = true;
    }

    public void setTelegramMessage(Long chatId, Integer messageId){
        this.telegramChatId = chatId;
        this.telegramMessageId = messageId;
    }

    public void setItemTextReady() {
        if(this.status == ContentStatus.READY || this.status == ContentStatus.REQUEST){
            this.status = ContentStatus.READY;
            this.items.clear();
        }
    }

    public void setItemTextReqeust(String gptModel) {
        if(this.status == ContentStatus.READY){
            this.itemTextGptModel = gptModel;
            this.status = ContentStatus.REQUEST;
        }
    }

    /**
     * 아이템 텍스트 활성 체크
     * @return
     */
    public boolean isItemTextActive(){
        return this.itemTextConfirmAgree 
            && this.itemTextStatus == ContentItemStatus.COMPLETED;   
    }

    /**
     * 아이템 이미지 활성 체크
     * @return
     */
    public boolean isItemImageActive(){
        return this.itemImageConfirmAgree 
            && this.itemImageStatus == ContentItemStatus.COMPLETED;   
    }

    /**
     * 아이템 음성 활성 체크
     * @return
     */
    public boolean isItemVoiceActive(){
        return this.itemVoiceConfirmAgree 
            && this.itemVoiceStatus == ContentItemStatus.COMPLETED;   
    }

    /**
     * BGM 활성 체크
     * @return
     */
    public boolean isBgmActive(){
        return this.bgmConfirmAgree
            && this.bgmStatus == ContentItemStatus.COMPLETED;
    }

    /**
     * 아이템 이미지 생성 가능 여부
     * @return
     */
    public boolean isGenerateItemImage(){
        return this.isItemTextActive() 
            && this.itemImageStatus != ContentItemStatus.COMPLETED;
    }

    /**
     * 아이템 이미지 생성 가능 여부
     * @return
     */
    public boolean isGenerateItemVoice(){
        return this.isItemTextActive()
            && this.itemVoiceStatus != ContentItemStatus.COMPLETED;
    }

    /**
     * 아이템 이미지 생성 가능 여부
     * @return
     */
    public boolean isGenerateBgm(){
        return this.isItemTextActive()
            && this.bgmStatus != ContentItemStatus.COMPLETED;
    }

    /**
     * 아이템 이미지 생성 가능 여부
     * @return
     */
    public boolean isGenerateVideo(){
        return this.isItemTextActive()
            && this.isItemImageActive()
            && this.isItemVoiceActive()
            && this.isBgmActive();
    }

    /** */
    public boolean isVideoUrl(){
        return StringUtils.hasText(this.videoUrl);
    }

    /** */
    public boolean isVideoTxId(){
        return StringUtils.hasText(this.videoUrl);
    }

    /** */
    public boolean isVideoCompleted() {
        return this.videoStatus == ContentItemStatus.COMPLETED
            && StringUtils.hasText(this.videoUrl)
            && StringUtils.hasText(this.videoTxId);
    }

    /** */
    public boolean isPublishCompleted() {
        return this.publishStatus == ContentPublishStatus.COMPLETED
            && StringUtils.hasText(this.publishYoutubeUrl);
    }

    /**
     * 아이템 텍스트 동의
     * @param agree
     */
    public void setItemConfirm(ContentItemType type, boolean agree) {
        switch (type) {
            case TEXT:
                this.itemTextConfirmAgree = agree;
                if(agree){
                    this.itemTextStatus = ContentItemStatus.COMPLETED;
                    this.itemTextConfirmDate = OffsetDateTime.now();
                } else {
                    this.itemTextStatus = ContentItemStatus.REJECT;
                    this.itemTextConfirmDate = null;
                }
                break;
                
            case IMAGE:
                this.itemImageConfirmAgree = agree;
                if(agree){
                    this.itemImageStatus = ContentItemStatus.COMPLETED;
                    this.itemImageConfirmDate = OffsetDateTime.now();
                } else {
                    this.itemImageStatus = ContentItemStatus.REJECT;
                    this.itemImageConfirmDate = null;
                }
            break;

            case VOICE:
                this.itemTextConfirmAgree = agree;
                if(agree){
                    this.itemVoiceStatus = ContentItemStatus.COMPLETED;
                    this.itemVoiceConfirmDate = OffsetDateTime.now();
                } else {
                    this.itemVoiceStatus = ContentItemStatus.REJECT;
                    this.itemVoiceConfirmDate = null;
                }
            break;
            
            default:
                break;
        }
    }

    public void setVideoConfirm(boolean agree) {
        this.videoConfirmAgree = agree;
        if(agree){
            this.videoConfirmDate = OffsetDateTime.now();
        } else {
            this.videoConfirmDate = null;
        }
    }
    
    public void setItemTextCreate(String subjectEng, List<ContentItem> items, boolean confirm) {
        this.subjectEng = subjectEng;
        this.items.addAll(items);
        if(confirm){
            this.itemTextStatus = ContentItemStatus.CONFIRM;
        } else {
            this.itemTextStatus = ContentItemStatus.COMPLETED;
        }
    }

    public void setItemImageCompleted() {
        this.itemImageStatus = ContentItemStatus.COMPLETED;
    }

    public void setImageModerated() {
        this.itemImageStatus = ContentItemStatus.MODERATED;
    }

    public void setItemImageFail() {
        this.itemImageStatus = ContentItemStatus.FAILED;
    }

    public void setItemVoiceCompleted() {
        this.itemVoiceStatus = ContentItemStatus.COMPLETED;
    }

    public void setItemVoiceFail() {
        this.itemVoiceStatus = ContentItemStatus.FAILED;
    }

    /** */
    public void resetVideoReady() {
        this.videoTxId = null;
        this.videoUrl = null;
        this.videoSnapshotUrl = null;
        this.videoConfirmAgree = false;
        this.videoStatus = ContentItemStatus.READY;
        this.videoRetryCount = 0;
    }

    /** */
    public void resetPublishReady() {
        this.publishYoutubeUrl = null;
        this.publishDate = null;
        this.publishStatus = ContentPublishStatus.READY;
    }

    /** */
    public void setVideoProcess(String videoTxId) {
        this.videoTxId = videoTxId;
        this.videoStatus = ContentItemStatus.PROCESS;
    }

    /** */
    public void setVideoProcessRetry(String videoTxId) {
        if(videoTxId.equals(this.videoTxId)){
            ++this.videoRetryCount;
        } else {
            this.videoTxId = videoTxId;
            this.videoRetryCount = 0;
        }
    }
    
    /** */
    public void setVideoFail(String message) {
        this.videoStatus = ContentItemStatus.FAILED;
        this.videoFailCause = message;
    }

    /** */
    public boolean isVideoFinish(boolean failRetry) {
        return this.videoStatus == ContentItemStatus.COMPLETED 
            || (!failRetry && this.videoStatus == ContentItemStatus.FAILED);
    }
   
    /** */
    public void setVideoCompleted(String videoTxId, String url, String snapshotUrl) {
        this.videoTxId = videoTxId;
        this.videoUrl = url;
        this.videoSnapshotUrl = snapshotUrl;
        this.videoConfirmAgree = true;
        this.videoStatus = ContentItemStatus.COMPLETED;
    }

     /** */
     public boolean isPublishFinish(boolean failRetry) {
        return this.publishStatus == ContentPublishStatus.COMPLETED 
            || (!failRetry && this.publishStatus == ContentPublishStatus.FAILED);
    }

    /** */
    public void setPublishFail() {
        this.publishStatus = ContentPublishStatus.FAILED;
    }

    /** */
    public void setPublishYoutubeCompleted(String url) {
        this.publishStatus = ContentPublishStatus.COMPLETED;
        this.publishYoutubeUrl = url;
        this.publishDate = OffsetDateTime.now();
    }

    public String getSocialShortTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.title);
        sb.append(" #").append(this.subject.replaceAll("[^가-힣a-zA-Z0-9]", ""));
        if(this.type != ContentType.NONE){
            sb.append(" #").append(this.type.getDesc());
        }
        return sb.toString();
    }

    public String getTelegramPrefixText() {
        StringBuilder sb =  new StringBuilder()
            .append("*컨텐츠*\n")
            .append(">> ID: ").append(this.getId()).append('\n')
            .append(">> 타입: ").append(this.getType().getDesc()).append('\n')
            .append(">> 주제: ").append(this.getSubject()).append('\n')
            .append(">> 상태: ").append(this.getStatus()).append('\n');

        if(!this.items.isEmpty()){
            sb.append("\n*내용*\n");
            for (ContentItem item : this.items) {
                sb.append(">> ").append(item.getText().replace("|", " | ")).append('\n');
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
