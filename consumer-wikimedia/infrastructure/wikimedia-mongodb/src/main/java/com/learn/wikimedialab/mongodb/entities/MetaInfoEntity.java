package com.learn.wikimedialab.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing metadata associated with a Wikimedia event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaInfoEntity {

  private String domain;
  private String uri;
  private String dt;
}
