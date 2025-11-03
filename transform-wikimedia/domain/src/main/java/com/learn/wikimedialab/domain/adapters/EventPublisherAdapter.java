package com.learn.wikimedialab.domain.adapters;

/**
 * Adapter interface for publishing events.
 */
public interface EventPublisherAdapter {

  /**
   * Publishes an event.
   *
   * @param event The event to be published.
   */
  void publish(String event);

}
