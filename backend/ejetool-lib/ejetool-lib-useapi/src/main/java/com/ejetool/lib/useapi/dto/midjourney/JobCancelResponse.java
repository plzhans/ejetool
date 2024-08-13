package com.ejetool.lib.useapi.dto.midjourney;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * 
 */
@Getter
public class JobCancelResponse {
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
     * 상태
     * {@link JobStatus}
     */
    private String status;

    /**
     * 
     * @param code
     * @param error
     * @param errorDetails
     * @return
     */
    public static JobCancelResponse createError(String jobId, int code, String error, String errorDetails) {
        var inst = new JobCancelResponse();
        inst.jobId = jobId;
        inst.code = code;
        inst.error = error;
        inst.errorDetails = errorDetails;
        return inst;
    }

    public boolean isSucceed(){
        return this.code == 200;
    }
}
