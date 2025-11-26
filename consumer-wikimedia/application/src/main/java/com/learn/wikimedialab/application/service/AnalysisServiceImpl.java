package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.exceptions.EventNotFoundException;
import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPort;
import com.learn.wikimedialab.domain.ports.out.OutboxPort;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.domain.values.OutboxEventType;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the AnalysisService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

  private final EventAnalysisPort eventAnalysisPort;

  private final OutboxPort outboxPort;

  private final WikimediaEventsPort wikimediaEventsPort;

  /**
   * Analyzes a Wikimedia event and stores the analysis result.
   *
   * @param eventAnalysis The event analysis to be processed.
   */
  @Override
  @Transactional
  public void analyzeEvent(EventAnalysis eventAnalysis) {
    log.info("Analyzing event: {}", eventAnalysis);
    this.validateEventAnalysis(eventAnalysis);

    final EventAnalysis eventAnalysisCreated = this.eventAnalysisPort.publishAnalysisEvent(
        eventAnalysis);
    this.outboxPort.saveEvent(this.createOutboxEvent(eventAnalysisCreated));
    log.info("Event analysis published: {}", eventAnalysisCreated);
  }

  /**
   * Validates the event analysis by checking if the associated Wikimedia event exists.
   *
   * @param eventAnalysis The event analysis to be validated.
   * @throws EventNotFoundException if the associated Wikimedia event does not exist.
   */
  private void validateEventAnalysis(EventAnalysis eventAnalysis) {
    if (!this.wikimediaEventsPort.existsWikimediaEventById(eventAnalysis.eventId())) {
      throw new EventNotFoundException();
    }
  }

  /**
   * Creates an outbox event for the given event analysis.
   *
   * @param eventAnalysis The event analysis for which to create the outbox event.
   * @return The created outbox event.
   */
  private Outbox<EventAnalysis> createOutboxEvent(EventAnalysis eventAnalysis) {
    return Outbox.<EventAnalysis>builder()
        .aggregateId(eventAnalysis.id())
        .aggregateType(eventAnalysis.getClass().getSimpleName())
        .eventType(OutboxEventType.CREATE)
        .payload(eventAnalysis)
        .status(OutboxStatus.PENDING)
        .build();
  }
}
