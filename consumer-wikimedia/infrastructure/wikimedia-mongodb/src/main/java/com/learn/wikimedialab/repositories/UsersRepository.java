package com.learn.wikimedialab.repositories;

import com.learn.wikimedialab.mongodb.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entities.
 */
@Repository
public interface UsersRepository extends MongoRepository<UserEntity, String> {

  /**
   * Finds a UserEntity by its username.
   *
   * @param username the username of the user
   * @return an Optional containing the UserEntity if found, otherwise empty
   */
  Optional<UserEntity> findByUsername(String username);
}
