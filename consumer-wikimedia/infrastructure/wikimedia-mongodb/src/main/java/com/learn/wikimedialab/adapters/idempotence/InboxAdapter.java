package com.learn.wikimedialab.adapters.idempotence;

import com.learn.wikimedialab.domain.ports.out.idempotence.InboxPort;
import com.learn.wikimedialab.mongodb.entities.idempotence.InboxEntity;
import com.learn.wikimedialab.repositories.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InboxAdapter implements InboxPort {

  private final InboxRepository inboxRepository;

  @Override
  public void insertProcessedEvent(String eventId) {
    log.debug("Saving processed event to inbox: {}", eventId);
    this.inboxRepository.insert(InboxEntity.builder().id(eventId).build());
    log.debug("Processed event saved to inbox: {}", eventId);
  }
}
