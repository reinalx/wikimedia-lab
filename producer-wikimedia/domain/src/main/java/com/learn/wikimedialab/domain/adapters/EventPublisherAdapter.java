package com.learn.wikimedialab.domain.adapters;

/**
 * Port interface for event publishing operations.
 */
public interface EventPublisherAdapter {

  /**
   * Publishes an event with the given data.
   *
   * @param eventData the data of the event to be published as JSON String
   */
  void publishEvent(String eventData);

}
