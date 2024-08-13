package com.ejetool.lib.youtube.service;

import com.ejetool.lib.youtube.dto.CreateYoutubeVideoParam;
import com.ejetool.lib.youtube.dto.YoutubeVideo;

public interface YoutubeService {
    public YoutubeVideo insert(CreateYoutubeVideoParam param);
}
