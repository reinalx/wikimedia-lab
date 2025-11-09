package com.learn.wikimedialab.mongodb.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing an analysis performed by a user on a Wikimedia event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event_analysis")
public class EventAnalysisEntity {

  @Id
  private String id;

  @Indexed
  private String eventId;

  private String analysis;

  @Indexed
  private String userId;

  private String sentiment;

  @Builder.Default
  private Long createdAt = System.currentTimeMillis();
}