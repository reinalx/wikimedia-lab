package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.User;

/**
 * Port interface for user-related operations.
 */
public interface UsersPort {

  /**
   * Finds a user by their username.
   *
   * @param username The username of the user to find.
   * @return The User entity if found, otherwise null.
   */
  User findByUsername(String username);
}
