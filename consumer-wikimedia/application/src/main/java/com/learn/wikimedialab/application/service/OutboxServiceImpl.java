package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.ports.in.services.OutboxService;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPublisherPort;
import com.learn.wikimedialab.domain.ports.out.idempotence.OutboxPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the OutboxService interface.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

  private final OutboxPort outboxPort;

  private final EventAnalysisPublisherPort eventAnalysisPublisherPort;

  @Override
  public void processPendingEvents() {
    log.info("Processing pending outbox events.");

    final List<Outbox<EventAnalysis>> pendingEvents = this.outboxPort.fetchPendingEvents();
    final List<Outbox<EventAnalysis>> eventsProcessed = this.publishEventAnalyses(pendingEvents);

    this.outboxPort.updateOutboxStatusToProcessed(eventsProcessed);
    log.info("Finished processing pending outbox events.");
  }


  private List<Outbox<EventAnalysis>> publishEventAnalyses(List<Outbox<EventAnalysis>> outboxes) {
    final List<String> publishedEventIds = this.eventAnalysisPublisherPort
        .publishAnalysisEvent(outboxes.stream()
            .map(Outbox::payload)
            .toList());

    return outboxes.stream()
        .filter(outbox -> publishedEventIds.contains(outbox.payload().id()))
        .toList();
  }


}
