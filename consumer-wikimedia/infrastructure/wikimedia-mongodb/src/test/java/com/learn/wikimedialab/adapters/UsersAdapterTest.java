package com.learn.wikimedialab.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.auth.User;
import com.learn.wikimedialab.mappers.UsersMapperImpl;
import com.learn.wikimedialab.mongodb.entities.UserEntity;
import com.learn.wikimedialab.repositories.UsersRepository;
import java.util.Optional;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class UsersAdapterTest {

  @InjectMocks
  private UsersAdapter usersAdapter;

  @Mock
  private UsersRepository usersRepository;

  @Mock
  private UsersMapperImpl usersMapper;


  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenUsername_whenFindByUsername_thenReturnUser(User user, UserEntity userEntity,
      String username) {
    // When
    when(this.usersRepository.findByUsername(username))
        .thenReturn(Optional.of(userEntity));
    when(this.usersMapper.toDomain(userEntity)).thenReturn(user);

    final User result = this.usersAdapter.findByUsername(username);

    // Then
    assertThat(result).isEqualTo(user);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenUsername_whenFindByUsername_thenReturnUser(String username) {
    // When
    when(this.usersRepository.findByUsername(username))
        .thenReturn(Optional.empty());

    final User result = this.usersAdapter.findByUsername(username);
    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenUser_whenCreateIfNotExist_thenSaveUser(User user, UserEntity userEntity) {
    // When
    when(this.usersMapper.toEntity(user)).thenReturn(userEntity);
    when(this.usersRepository.findByUsername(userEntity.getUsername()))
        .thenReturn(Optional.empty());

    this.usersAdapter.createIfNotExist(user);

    // Then
    verify(this.usersRepository).save(userEntity);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenUser_whenCreateIfNotExist_thenDontSave(User user, UserEntity userEntity,
      UserEntity existingUser) {
    // When
    when(this.usersMapper.toEntity(user)).thenReturn(userEntity);
    when(this.usersRepository.findByUsername(userEntity.getUsername()))
        .thenReturn(Optional.of(existingUser));

    this.usersAdapter.createIfNotExist(user);
    // Then
    verify(this.usersRepository).findByUsername(userEntity.getUsername());
    verify(this.usersRepository, times(0)).save(userEntity);

  }

}