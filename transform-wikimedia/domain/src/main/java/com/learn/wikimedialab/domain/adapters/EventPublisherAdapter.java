package com.learn.wikimedialab.domain.adapters;

import com.learn.wikimedialab.domain.events.WikimediaEvent;

/**
 * Adapter interface for publishing events.
 */
public interface EventPublisherAdapter {

  /**
   * Publishes a wikimedia event.
   *
   * @param event The event to be published.
   */
  void publish(WikimediaEvent event);

}
