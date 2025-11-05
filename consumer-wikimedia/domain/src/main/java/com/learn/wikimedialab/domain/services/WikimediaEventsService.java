package com.learn.wikimedialab.domain.services;

import com.learn.wikimedialab.domain.events.WikimediaEvent;

/**
 * Service interface for processing Wikimedia events.
 */
public interface WikimediaEventsService {

  /**
   * Processes a Wikimedia event.
   *
   * @param event The Wikimedia event to be processed.
   */
  void processEvent(WikimediaEvent event);

}
