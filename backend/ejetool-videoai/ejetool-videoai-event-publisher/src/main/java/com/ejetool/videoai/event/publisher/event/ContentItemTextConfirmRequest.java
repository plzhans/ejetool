// package com.ejetool.videoai.event.publisher.event;

// import com.ejetool.mq.event.BaseEvent;
// import com.fasterxml.jackson.annotation.JsonCreator;
// import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
// import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

// import lombok.Builder;
// import lombok.Getter;

// @JsonDeserialize(builder = ContentItemTextConfirmRequest.JsonBuilder.class)
// public class ContentItemTextConfirmRequest extends BaseEvent {

//     @Getter
//     @JsonProperty("content_id") 
//     private final long contentId;

    
//     @Getter
//     @JsonProperty("agree") 
//     private final boolean agree;

//     @Builder
//     @JsonCreator
//     public ContentItemTextConfirmRequest(
//         @JsonProperty("request_id") String requestId,
//         @JsonProperty("timestamp") long timestamp, 
//         @JsonProperty("content_id") long contentId,
//         @JsonProperty("agree") boolean agree
        
//     ){
//         super(requestId, timestamp);
//         this.contentId = contentId;
//         this.agree = agree;
//     }

//     @JsonPOJOBuilder(withPrefix = "")
//     public static class JsonBuilder {}
// }
