package com.learn.wikimedialab.domain.adapters;

/**
 * Adapter interface for consuming events.
 */
public interface EventConsumerAdapter {

  /**
   * Consumes an event.
   *
   * @param event The event to be consumed.
   */
  void consumer(String event);
}
