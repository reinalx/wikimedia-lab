package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import java.util.List;

/**
 * Outbound port interface for the outbox pattern.
 */
public interface OutboxPort {

  /**
   * Saves an event to the outbox.
   *
   * @param event The outbox event to be saved.
   */
  void saveEvent(Outbox<EventAnalysis> event);

  /**
   * Updates the status of outbox events to PROCESSED.
   *
   * @param outboxes The list of outbox events to be updated.
   */
  void updateOutboxStatusToProcessed(List<Outbox<EventAnalysis>> outboxes);


  /**
   * Processes pending outbox events.
   */
  List<Outbox<EventAnalysis>> fetchPendingEvents();


}
