package com.learn.wikimedialab.domain.services;

import com.learn.wikimedialab.domain.events.WikimediaEvent;

/**
 * Service interface for processing Wikimedia events.
 */
public interface WikimediaProcessorService {

  /**
   * Processes a Wikimedia event.
   *
   * @param wikimediaEvent the Wikimedia event to process
   */
  void processEvent(WikimediaEvent wikimediaEvent);

}
