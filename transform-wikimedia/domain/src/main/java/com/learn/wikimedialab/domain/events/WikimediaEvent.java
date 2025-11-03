package com.learn.wikimedialab.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Record representing a Wikimedia event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WikimediaEvent(
    String id,
    String type,
    String user,
    boolean bot,
    Meta meta
) {
  /**
   * Metadata associated with the Wikimedia event.
   */
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Meta {
    private String domain;
  }
}
