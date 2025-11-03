package com.learn.wikimedialab.domain.services;

/**
 * Service interface for processing Wikimedia events.
 */
public interface WikimediaProcessorService {

  /**
   * Processes a raw Wikimedia event.
   *
   * @param rawEvent the raw event data as a String
   */
  void processEvent(String rawEvent);

}
