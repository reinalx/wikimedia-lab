package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.domain.values.OutboxStatus;
import com.learn.wikimedialab.mongodb.entities.idempotence.OutboxEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing OutboxEntity documents in MongoDB.
 */
@Repository
public interface OutboxRepository extends MongoRepository<OutboxEntity, String> {

  /**
   * Finds outbox entities by their status.
   *
   * @param status The status of the outbox entities to find.
   * @return A list of outbox entities with the specified status.
   */
  List<OutboxEntity> findByStatus(OutboxStatus status);
}
