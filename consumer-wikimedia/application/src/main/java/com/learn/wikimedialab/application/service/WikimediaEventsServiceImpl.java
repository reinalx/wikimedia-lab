package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.adapters.WikimediaEventsAdapter;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.domain.services.WikimediaEventsService;
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
public class WikimediaEventsServiceImpl implements WikimediaEventsService {

  private final WikimediaEventsAdapter wikimediaEventsAdapter;

  @Override
  public void processEvent(WikimediaEvent event) {
    log.info("Processing event for saving: {}", event);

    this.wikimediaEventsAdapter.saveFilteredEvent(event);

    log.info("Event saved to database: {}", event);
  }

}
