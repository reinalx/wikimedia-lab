package com.learn.wikimedialab.domain.ports.in.services;

/**
 * Service interface for processing outbox events.
 */
public interface OutboxService {

  /**
   * Processes pending outbox events.
   */
  void processPendingEvents();

}
