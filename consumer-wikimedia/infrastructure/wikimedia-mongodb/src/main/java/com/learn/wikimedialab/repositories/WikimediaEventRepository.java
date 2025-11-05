package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.mongodb.entities.WikimediaEventEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for Wikimedia events.
 */
public interface WikimediaEventRepository extends MongoRepository<WikimediaEventEntity, String> {

  /**
   * Finds all Wikimedia event entities with pagination.
   *
   * @param pageable the pagination information
   * @return a list of Wikimedia event entities
   */
  List<WikimediaEventEntity> findAllBy(Pageable pageable);

}
