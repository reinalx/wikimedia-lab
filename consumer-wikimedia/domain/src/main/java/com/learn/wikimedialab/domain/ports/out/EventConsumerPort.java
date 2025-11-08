package com.learn.wikimedialab.domain.ports.out;

/**
 * Adapter interface for consuming events.
 */
public interface EventConsumerPort {

  /**
   * Consumes an event.
   *
   * @param event The event to be consumed.
   */
  void consumer(String event);
}
