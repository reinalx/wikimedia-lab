package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.ports.in.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UsersService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UsersService {

  /**
   * Logs in a user with the given username and password.
   *
   * @param username the username of the user
   * @param password the password of the user
   * @return a JWT token if login is successful
   */
  @Override
  public String loginUser(String username, String password) {
    return "";
  }
}
