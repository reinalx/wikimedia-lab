package com.learn.wikimedialab.domain.adapters;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import java.util.List;

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

  /**
   * Retrieves a list of Wikimedia events with pagination.
   *
   * @param page The page number.
   * @param size The number of events per page.
   * @return A list of Wikimedia events.
   */
  List<WikimediaEvent> getWikimediaEvents(int page, int size);

}
