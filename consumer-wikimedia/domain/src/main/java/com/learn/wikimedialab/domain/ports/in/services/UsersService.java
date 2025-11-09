package com.learn.wikimedialab.domain.ports.in.services;

/**
 * Service interface for managing users.
 */
public interface UsersService {

  /**
   * Logs in a user with the given username and password.
   *
   * @param username the username of the user
   * @param password the password of the user
   * @return a JWT token if login is successful
   */
  String loginUser(String username, String password);

}
