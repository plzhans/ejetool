package com.ejetool.videoai.event.publisher.publisher;

import lombok.experimental.UtilityClass;

/**
 * VideoAI Content StreamKey
 */
@SuppressWarnings("squid:S1192")
@UtilityClass
public class VideoAIContentStreamKey {
    // default
    public static final String DEFAULT = "videoai:content";

    // content
    public static final String CREATED = DEFAULT + ":created";

    // item text
    public static final String ITEM_TEXT = DEFAULT + ":item-text";
    public static final String ITEM_TEXT_GENERATE_REQUEST = ITEM_TEXT + ":generate-request";
    public static final String ITEM_TEXT_CREATED = ITEM_TEXT + ":completed";
    public static final String ITEM_TEXT_CONFIRM_REQUEST = ITEM_TEXT + ":confirm-request";

    // item voice
    public static final String ITEM_VOICE = DEFAULT + ":item-voice";
    public static final String ITEM_VOICE_GENERATE_REQUEST = ITEM_VOICE + ":generate-request";
    public static final String ITEM_VOICE_CREATED = ITEM_VOICE + ":completed";

    // item image
    public static final String ITEM_IMAGE = DEFAULT + ":item-image";
    public static final String ITEM_IMAGE_GENERATE_REQUEST = ITEM_IMAGE + ":generate-request";
    public static final String ITEM_IMAGE_CREATED = ITEM_IMAGE + ":completed";

    // video
    public static final String VIDEO = DEFAULT + ":video";
    public static final String VIDEO_GENERATE_REQUEST = VIDEO + ":generate-request";
    public static final String VIDEO_CREATED = VIDEO + ":completed";

    // publish
    public static final String PUBLISH = DEFAULT+":publish";
    public static final String PUBLISH_REQUEST = PUBLISH + ":request";
    public static final String PUBLISH_COMPLETED = PUBLISH + ":completed";
}
