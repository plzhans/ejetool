package com.ejetool.lib.youtube.dto;

import com.ejetool.lib.youtube.consts.YoutubeVideoLicense;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YoutubeVideoStatus {
    private Boolean embeddable;

    private String failureReason;

    private String license = YoutubeVideoLicense.YOUTUBE;

    private Boolean madeForKids;

    private Boolean publicStatsViewable;

    private String privacyStatus;

    //private DateTime publishAt;

    private String rejectionReason;

    private Boolean selfDeclaredMadeForKids;

    private String uploadStatus;

}
