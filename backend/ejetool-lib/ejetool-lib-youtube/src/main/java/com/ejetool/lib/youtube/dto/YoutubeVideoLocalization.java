package com.ejetool.lib.youtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YoutubeVideoLocalization {
    /**
     * Localized version of the video's description.
     * The value may be {@code null}.
     */
    private String description;

    /**
     * Localized version of the video's title.
     * The value may be {@code null}.
     */
    private String title;
}
