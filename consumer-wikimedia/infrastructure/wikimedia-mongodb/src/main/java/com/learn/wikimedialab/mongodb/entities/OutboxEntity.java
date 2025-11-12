package com.learn.wikimedialab.mongodb.entities;


import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing an outbox event for event sourcing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "outbox")
public class OutboxEntity {

  @Id
  private String id;

  private String aggregateId;

  private String aggregateType;

  private String eventType;

  private String payload;

  private OffsetDateTime createdAt;
  
  private String status;

}
