package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.mongodb.entities.OutboxEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing OutboxEntity documents in MongoDB.
 */
@Repository
public interface OutboxRepository extends MongoRepository<OutboxEntity, String> {

}
