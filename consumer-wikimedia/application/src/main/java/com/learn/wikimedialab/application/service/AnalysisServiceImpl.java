package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.exceptions.EventNotFoundException;
import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPort;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.domain.ports.out.idempotence.OutboxPort;
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

  private void validateEventAnalysis(EventAnalysis eventAnalysis) {
    if (!this.wikimediaEventsPort.existsWikimediaEventById(eventAnalysis.eventId())) {
      throw new EventNotFoundException();
    }
  }


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
