package com.learn.wikimedialab.application.service;

import static com.learn.wikimedialab.domain.utils.Constants.SPANISH_WIKIPEDIA_DOMAIN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.domain.services.WikimediaProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the WikimediaProcessorService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WikimediaProcessorServiceImpl implements WikimediaProcessorService {

  private final EventPublisherAdapter eventPublisherAdapter;
  private final ObjectMapper mapper = new ObjectMapper();


  /**
   * Processes a Wikimedia event.
   *
   * @param event the Wikimedia event
   */
  @Override
  public void processEvent(WikimediaEvent event) {
    if (!this.shouldBeForwarded(event)) {
      log.debug("Event discarded: type={}, bot={}, domain={}",
          event.type(), event.bot(),
          event.meta() != null ? event.meta().domain() : null);
      return;
    }
    this.eventPublisherAdapter.publish(event);
    log.info("Valid event sent: {}", event.user());
  }

  /**
   * Determines if the event should be forwarded based on specific criteria.
   *
   * @param event the Wikimedia event
   * @return true if the event should be forwarded, false otherwise
   */
  private boolean shouldBeForwarded(WikimediaEvent event) {
    if (event == null || event.meta() == null) {
      return false;
    }

    final boolean isEdit = "edit".equalsIgnoreCase(event.type());
    final boolean isNotBot = !event.bot();
    final boolean isSpanish = event.meta().domain() != null
        && event.meta().domain().contains(SPANISH_WIKIPEDIA_DOMAIN);

    return isEdit && isNotBot && isSpanish;
  }
}
