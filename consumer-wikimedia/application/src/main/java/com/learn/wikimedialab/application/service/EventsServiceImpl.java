package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the WikimediaEventsService interface.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

  private final WikimediaEventsPort wikimediaEventsAdapter;

  @Override
  public void processEvent(WikimediaEvent event) {
    log.info("Processing event for saving: {}", event);

    this.wikimediaEventsAdapter.saveFilteredEvent(event);

    log.info("Event saved to database: {}", event);
  }


  @Override
  public List<WikimediaEvent> getWikimediaEvents(int page, int size) {
    log.info("Retrieving Wikimedia events - Page: {}, Size: {}", page, size);
    return this.wikimediaEventsAdapter.getWikimediaEvents(page, size);
  }

}
