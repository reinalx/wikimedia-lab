package com.learn.wikimedialab.mappers;

import com.learn.wikimedialab.domain.entities.User;
import com.learn.wikimedialab.mongodb.entities.UserEntity;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between UserEntity and User domain entity.
 */
@Mapper(componentModel = "spring")
public interface UsersMapper {

  /**
   * Maps a UserEntity to a User domain entity.
   *
   * @param userEntity the UserEntity to map
   * @return the mapped User domain entity
   */
  User toDomain(UserEntity userEntity);

}
