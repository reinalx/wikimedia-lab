package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.mongodb.entities.WikimediaEventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for Wikimedia events.
 */
public interface WikimediaEventRepository extends MongoRepository<WikimediaEventEntity, String> {

}
