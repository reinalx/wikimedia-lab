package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.ports.out.OutboxPort;
import com.learn.wikimedialab.mappers.OutboxMapper;
import com.learn.wikimedialab.repositories.OutboxRepository;
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

  @Override
  public void saveEvent(Outbox<?> event) {
    log.info("Saving outbox event: {}", event);
    this.outboxRepository.save(
        this.outboxMapper.toPersistence(event)
    );
    log.info("Outbox event saved: {}", event);
  }
}
