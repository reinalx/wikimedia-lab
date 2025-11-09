package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.JwtInfo;
import com.learn.wikimedialab.domain.entities.User;
import com.learn.wikimedialab.domain.exceptions.PasswordMismatchingException;
import com.learn.wikimedialab.domain.exceptions.UserNotFoundException;
import com.learn.wikimedialab.domain.ports.in.services.JwtService;
import com.learn.wikimedialab.domain.ports.in.services.UsersService;
import com.learn.wikimedialab.domain.ports.out.UsersPort;
import com.learn.wikimedialab.domain.values.RoleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UsersService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UsersService {

  private final UsersPort usersPort;

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  /**
   * Logs in a user with the given username and password.
   *
   * @param username the username of the user
   * @param password the password of the user
   * @return a JWT token if login is successful
   */
  @Override
  public String loginUser(String username, String password) {
    log.info("Attempting to log in user: {}", username);

    final User user = this.getUserByUsername(username);
    this.checkMatchPassword(password, user.password());
    return this.generateTokenForUser(user);
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username the username of the user
   * @return the User entity
   * @throws UserNotFoundException if the user is not found
   */
  private User getUserByUsername(String username) {
    final User user = this.usersPort.findByUsername(username);
    if (user == null) {
      log.error("User not found: {}", username);
      throw new UserNotFoundException();
    }
    return user;
  }

  /**
   * Checks if the provided password matches the expected password.
   *
   * @param actualPassword   the actual password provided by the user
   * @param expectedPassword the expected password stored in the system
   * @throws BadCredentialsException if the passwords do not match
   */
  private void checkMatchPassword(String actualPassword, String expectedPassword) {
    if (!this.passwordEncoder.matches(actualPassword, expectedPassword)) {
      throw new PasswordMismatchingException();
    }
  }

  /**
   * Generates a JWT token for the given user.
   *
   * @param user the User entity
   * @return a JWT token
   */
  private String generateTokenForUser(User user) {
    return this.jwtService.generateToken(
        JwtInfo.builder()
            .userId(user.id())
            .username(user.username())
            .role(user.role())
            .build()
    );
  }


  /**
   * Initializes user data by creating a default reader user if it does not exist.
   */
  @PostConstruct
  public void userDataInitializer() {
    log.info("Initializing user data...");
    this.usersPort.createIfNotExist(
        User.builder()
            .username("reader_user")
            .password(this.passwordEncoder.encode("reader_password"))
            .role(RoleType.READER.toString())
            .build()
    );
    this.usersPort.createIfNotExist(
        User.builder()
            .username("editor_user")
            .password(this.passwordEncoder.encode("editor_password"))
            .role(RoleType.EDITOR.toString())
            .build()
    );
    log.info("User data initialization complete.");
  }
}
