package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.ports.in.services.OutboxService;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPublisherPort;
import com.learn.wikimedialab.domain.ports.out.OutboxPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    final List<Outbox<?>> pendingEvents = this.outboxPort.fetchPendingEvents();

    this.eventAnalysisPublisherPort.publishAnalysisEvent(
        pendingEvents.stream().filter(Outbox::isPayloadEventAnalysis)
            .map(Outbox::payload).toList());
    log.info("Finished processing pending outbox events.");
  }
}
