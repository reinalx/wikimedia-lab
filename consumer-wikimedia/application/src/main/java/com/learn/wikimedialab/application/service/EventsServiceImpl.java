package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.domain.ports.out.idempotence.InboxPort;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

  private final WikimediaEventsPort wikimediaEventsAdapter;

  private final InboxPort inboxPort;

  @Override
  @Retry(name = "mongoRetry")
  public void processEvent(WikimediaEvent event) {
    log.info("Processing event for saving: {}", event);

    try {
      this.inboxPort.insertProcessedEvent(event.id());
    } catch (final DuplicateKeyException e) {
      log.warn("Event with ID {} has already been processed. Skipping.", event.id());
      return;
    }

    this.wikimediaEventsAdapter.saveFilteredEvent(event);

    log.info("Event saved to database: {}", event);
  }


  @Override
  public List<WikimediaEvent> getWikimediaEvents(int page, int size) {
    log.info("Retrieving Wikimedia events - Page: {}, Size: {}", page, size);
    return this.wikimediaEventsAdapter.getWikimediaEvents(page, size);
  }

}
