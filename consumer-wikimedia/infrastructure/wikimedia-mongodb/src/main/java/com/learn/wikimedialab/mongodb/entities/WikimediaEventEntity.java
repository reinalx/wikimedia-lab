package com.learn.wikimedialab.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing a Wikimedia event stored in MongoDB.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wikimedia_events")
public class WikimediaEventEntity {

  @Id
  private String id;

  private String type;

  @Indexed
  private String user;

  private String title;

  private String titleUrl;

  private String comment;

  private boolean bot;

  private MetaInfoEntity meta;

  @Builder.Default
  private Long createdAt = System.currentTimeMillis();

}