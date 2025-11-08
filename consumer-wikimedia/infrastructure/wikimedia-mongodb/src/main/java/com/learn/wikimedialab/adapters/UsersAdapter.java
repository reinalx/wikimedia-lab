package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.User;
import com.learn.wikimedialab.domain.ports.out.UsersPort;
import com.learn.wikimedialab.mappers.UsersMapper;
import com.learn.wikimedialab.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter implementation for UsersPort.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UsersAdapter implements UsersPort {

  private final UsersRepository usersRepository;

  private final UsersMapper usersMapper;

  @Override
  public User findByUsername(String username) {
    log.info("Finding user by username: {}", username);
    return this.usersRepository.findByUsername(username)
        .map(this.usersMapper::toDomain)
        .orElse(null);
  }
}
