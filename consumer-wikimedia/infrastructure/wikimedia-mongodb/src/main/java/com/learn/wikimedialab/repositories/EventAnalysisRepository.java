package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.mongodb.entities.EventAnalysisEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing EventAnalysisEntity objects in MongoDB.
 */
@Repository
public interface EventAnalysisRepository extends MongoRepository<EventAnalysisEntity, String> {

}
