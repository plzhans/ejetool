package com.ejetool.lib.youtube.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class YoutubeVideoSnippet {
  /**
   * The YouTube video category associated with the video.
   * The value may be {@code null}.
   */
  private String categoryId;

  /**
   * The ID that YouTube uses to uniquely identify the channel that the video was uploaded to.
   * The value may be {@code null}.
   */
  private String channelId;

  /**
   * Channel title for the channel that the video belongs to.
   * The value may be {@code null}.
   */
  private String channelTitle;

  /**
   * The default_audio_language property specifies the language spoken in the video's default audio
   * track.
   * The value may be {@code null}.
   */
  private String defaultAudioLanguage;

  /**
   * The language of the videos's default snippet.
   * The value may be {@code null}.
   */
  private String defaultLanguage;

  /**
   * The video's description. @mutable youtube.videos.insert youtube.videos.update
   * The value may be {@code null}.
   */
  private String description;

  /**
   * Indicates if the video is an upcoming/active live broadcast. Or it's "none" if the video is not
   * an upcoming/active live broadcast.
   * The value may be {@code null}.
   */
  private String liveBroadcastContent;

  /**
   * Localized snippet selected with the hl parameter. If no such localization exists, this field is
   * populated with the default snippet. (Read-only)
   * The value may be {@code null}.
   */
  private YoutubeVideoLocalization localized;

//   /**
//    * The date and time when the video was uploaded.
//    * The value may be {@code null}.
//    */
//   private DateTime publishedAt;

  /**
   * A list of keyword tags associated with the video. Tags may contain spaces.
   * The value may be {@code null}.
   */
  private List<String> tags;

//   /**
//    * A map of thumbnail images associated with the video. For each object in the map, the key is the
//    * name of the thumbnail image, and the value is an object that contains other information about
//    * the thumbnail.
//    * The value may be {@code null}.
//    */
//   private ThumbnailDetails thumbnails;

  /**
   * The video's title. @mutable youtube.videos.insert youtube.videos.update
   * The value may be {@code null}.
   */
  private String title;

}
