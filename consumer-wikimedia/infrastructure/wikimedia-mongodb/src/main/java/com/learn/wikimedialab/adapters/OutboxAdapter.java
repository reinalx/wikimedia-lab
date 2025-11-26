package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.ports.out.OutboxPort;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import com.learn.wikimedialab.mappers.OutboxMapper;
import com.learn.wikimedialab.mongodb.entities.OutboxEntity;
import com.learn.wikimedialab.repositories.OutboxRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * Adapter implementation for the OutboxPort interface.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxAdapter implements OutboxPort {

  private final OutboxRepository outboxRepository;

  private final OutboxMapper outboxMapper;

  /**
   * Saves an outbox event to the repository.
   *
   * @param event The outbox event to be saved.
   */
  @Override
  public void saveEvent(Outbox<EventAnalysis> event) {
    log.info("Saving outbox event: {}", event);
    this.outboxRepository.save(
        this.outboxMapper.toPersistence(event)
    );
    log.info("Outbox event saved: {}", event);
  }

  /**
   * Fetches pending outbox events from the repository.
   *
   * @return A list of pending outbox events.
   */
  @Override
  public List<Outbox<EventAnalysis>> fetchPendingEvents() {
    log.info("Fetching pending outbox events.");
    return this.outboxMapper.toDomainList(
        this.outboxRepository.findByStatus(OutboxStatus.PENDING));
  }

  /**
   * Updates the status of outbox events to PROCESSED.
   *
   * @param outboxes The list of outbox events to be updated.
   */
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
