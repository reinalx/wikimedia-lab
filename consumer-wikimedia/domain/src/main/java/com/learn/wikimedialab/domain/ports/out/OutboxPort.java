package com.learn.wikimedialab.domain.ports.out;

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
  void saveEvent(Outbox<?> event);

  /**
   * Processes pending outbox events.
   */
  List<Outbox<?>> fetchPendingEvents();

}
