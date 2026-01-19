package com.learn.wikimedialab.domain.entities.outbox;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.values.OutboxEventType;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import lombok.Builder;

/**
 * Record representing an outbox object for event sourcing.
 */
@Builder(toBuilder = true)
public record Outbox<T>(
    String id,
    String aggregateId,
    String aggregateType,
    OutboxEventType eventType,
    T payload,
    OutboxStatus status
) {

  /**
   * Checks if the payload is of type EventAnalysis.
   *
   * @return true if the payload is an instance of EventAnalysis, false otherwise.
   */
  public Boolean isPayloadEventAnalysis() {
    return this.payload != null && this.aggregateType.equals(EventAnalysis.class.getSimpleName());
  }
}
