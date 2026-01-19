package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.auth.JwtInfo;
import com.learn.wikimedialab.domain.entities.auth.User;
import com.learn.wikimedialab.domain.exceptions.PasswordMismatchingException;
import com.learn.wikimedialab.domain.exceptions.UserNotFoundException;
import com.learn.wikimedialab.domain.ports.in.services.JwtService;
import com.learn.wikimedialab.domain.ports.in.services.UsersService;
import com.learn.wikimedialab.domain.ports.out.UsersPort;
import com.learn.wikimedialab.domain.values.RoleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Override
  public String loginUser(String username, String password) {
    log.info("Attempting to log in user: {}", username);

    final User user = this.getUserByUsername(username);
    this.checkMatchPassword(password, user.password());
    return this.generateTokenForUser(user);
  }

  private User getUserByUsername(String username) {
    final User user = this.usersPort.findByUsername(username);
    if (user == null) {
      log.error("User not found: {}", username);
      throw new UserNotFoundException();
    }
    return user;
  }

  private void checkMatchPassword(String actualPassword, String expectedPassword) {
    if (!this.passwordEncoder.matches(actualPassword, expectedPassword)) {
      throw new PasswordMismatchingException();
    }
  }

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
