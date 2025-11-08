package com.learn.wikimedialab.domain.ports.in.services;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import java.util.List;

/**
 * Service interface for processing Wikimedia events.
 */
public interface EventsService {

  /**
   * Processes a Wikimedia event.
   *
   * @param event The Wikimedia event to be processed.
   */
  void processEvent(WikimediaEvent event);

  /**
   * Retrieves a list of Wikimedia events with pagination.
   *
   * @param page The page number.
   * @param size The number of events per page.
   * @return A list of Wikimedia events.
   */
  List<WikimediaEvent> getWikimediaEvents(int page, int size);

}
