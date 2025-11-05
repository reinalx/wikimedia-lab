package com.learn.wikimedialab.domain.adapters;

import com.learn.wikimedialab.domain.events.WikimediaEvent;

/**
 * Adapter interface for filtering events.
 */
public interface WikimediaEventsAdapter {

  /**
   * Saves a filtered Wikimedia event.
   *
   * @param event The filtered Wikimedia event to be saved.
   */
  void saveFilteredEvent(WikimediaEvent event);

}
