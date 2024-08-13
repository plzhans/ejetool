package com.ejetool.lib.useapi.dto.midjourney;

import lombok.experimental.UtilityClass;

/**
 *
 */
@UtilityClass
public class JobStatus {
    public static final String CREATED = "created";
    public static final String STARTED = "started";
    public static final String MODERATED = "moderated";
    public static final String PROGRESS = "progress";
    public static final String COMPLETED = "completed";
    public static final String FAILED = "failed";
    public static final String CANCELLED = "cancelled";
}
