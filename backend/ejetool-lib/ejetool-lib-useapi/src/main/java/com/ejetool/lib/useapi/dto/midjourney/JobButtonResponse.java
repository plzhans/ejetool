package com.ejetool.lib.useapi.dto.midjourney;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * status : 200
 * {
 *     "jobid": "<jobid>",
 *     "verb": "button",
 *     "status": "started",
 *     "created": "2023-09-09T20:19:57.073Z",
 *     "updated": "2023-09-09T20:19:58.536Z",
 *     "button": "V1",
 *     "parentJobId": "<jobid>",
 *     "discord": "<ABC…secured…xyz>",
 *     "channel": "<Discord channel id>",
 *     "server": "<Discord server id>",
 *     "maxJobs": 3,
 *     "replyUrl": "https://webhook.site/abc",
 *     "replyRef": "<your optional reference id>",
 *     "content": "<Discord message content>",
 *     "messageId": "<Discord message id>",
 *     "timestamp": "2023-09-09T20:19:57.926000+00:00",
 *     "code": 200
 * }
 */
@Getter
public class JobButtonResponse {
    /**
     * 
     */
    private int code;

    /**
     * 
     */
    private String error;

    /**
     * 
     */
    private String errorDetails;

    /**
     * 
     */
    @JsonProperty("jobid")
    private String jobId;

    /**
     * verb
     * {@link JobVerb}
     */
    private String verb;
    
    /**
     * 상태
     * {@link JobStatus}
     */
    private String status;

    /**
     * YYYY-MM-DDTHH:mm:ss.sssZ, IS0 8601, UTC
     */
    private String created;

    /**
     * YYYY-MM-DDTHH:mm:ss.sssZ, IS0 8601, UTC
     */
    private String updated;
    
    /** 
     * 버튼
     * {@link JobButton} 
     */
    private String button;

    /**
     * 
     */
    @JsonProperty("parentJobId")
    private String parentJobId;

    /**
     * Provided for debugging purposes only, contains the first 3 and the last 3 characters of the original value
     */
    private String discord;

    /**
     * 
     */
    private String channel;

    /**
     * 
     */
    private String server;

    /**
     * 
     */
    private int maxJobs;

    /**
     * 
     */
    private String replyUrl;

    /**
     * 
     */
    private String replyRef;

    /**
     * 
     */
    private String messageId;

    /**
     * 2023-09-09T20:19:57.926000+00:00
     */
    private String timestamp;

    /**
     * 
     * @param code
     * @param error
     * @param errorDetails
     * @return
     */
    public static JobButtonResponse createError(int code, String error, String errorDetails) {
        var inst = new JobButtonResponse();
        inst.code = code;
        inst.error = error;
        inst.errorDetails = errorDetails;
        return inst;
    }

    public boolean isSucceed(){
        return this.code == 200;
    }
}
