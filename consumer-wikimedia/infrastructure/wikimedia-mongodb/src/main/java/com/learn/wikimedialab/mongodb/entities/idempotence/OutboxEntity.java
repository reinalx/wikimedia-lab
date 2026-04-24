package com.learn.wikimedialab.mongodb.entities.idempotence;


import com.learn.wikimedialab.domain.values.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing an outbox event for event sourcing.
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "outbox")
public class OutboxEntity {

  @Id
  private String id;

  private String aggregateId;

  private String aggregateType;

  private String eventType;

  private Object payload;

  @Builder.Default
  private Long createdAt = System.currentTimeMillis();

  private OutboxStatus status;

}
