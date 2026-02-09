package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.mongodb.entities.idempotence.InboxEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends MongoRepository<InboxEntity, String> {

}
