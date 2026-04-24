package com.learn.wikimedialab.adapters.idempotence;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.ports.out.idempotence.OutboxPort;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import com.learn.wikimedialab.mappers.idempotence.OutboxMapper;
import com.learn.wikimedialab.mongodb.entities.idempotence.OutboxEntity;
import com.learn.wikimedialab.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxAdapter implements OutboxPort {

  private final OutboxRepository outboxRepository;

  private final OutboxMapper outboxMapper;

  @Override
  public void saveEvent(Outbox<EventAnalysis> event) {
    log.info("Saving outbox event: {}", event);
    this.outboxRepository.save(
        this.outboxMapper.toPersistence(event)
    );
    log.info("Outbox event saved: {}", event);
  }

  @Override
  public List<Outbox<EventAnalysis>> fetchPendingEvents() {
    log.info("Fetching pending outbox events.");
    return this.outboxMapper.toDomainList(
        this.outboxRepository.findByStatus(OutboxStatus.PENDING));
  }


  @Override
  public void updateOutboxStatusToProcessed(List<Outbox<EventAnalysis>> outboxes) {
    log.info("Updating outbox events to PROCESSED: {}", outboxes);
    final List<OutboxEntity> persistenceOutboxes = this.outboxRepository.findAllById(
        outboxes.stream()
            .map(Outbox::id)
            .toList()
    );

    persistenceOutboxes.forEach(outbox -> outbox.setStatus(OutboxStatus.PROCESSED));

    this.outboxRepository.saveAll(persistenceOutboxes);
    log.info("Outbox events updated to PROCESSED: {}", outboxes);
  }


}
