package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.events.WikimediaEvent;
import java.util.List;

/**
 * Adapter interface for filtering events.
 */
public interface WikimediaEventsPort {

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

  /**
   * Checks if a Wikimedia event exists by its ID.
   *
   * @param id The ID of the Wikimedia event.
   * @return true if the event exists, false otherwise.
   */
  boolean existsWikimediaEventById(String id);

}
