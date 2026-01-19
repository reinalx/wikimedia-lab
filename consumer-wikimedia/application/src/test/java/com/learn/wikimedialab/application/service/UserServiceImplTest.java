package com.learn.wikimedialab.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.auth.JwtInfo;
import com.learn.wikimedialab.domain.entities.auth.User;
import com.learn.wikimedialab.domain.exceptions.UserNotFoundException;
import com.learn.wikimedialab.domain.ports.in.services.JwtService;
import com.learn.wikimedialab.domain.ports.out.UsersPort;
import com.learn.wikimedialab.domain.values.ErrorCode;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Mock
  private UsersPort usersPort;

  @Mock
  private JwtService jwtService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenUsernameAndPassword_whenLoginUser_thenReturnToken(String username, String password,
      String token, User user) {
    // Given
    final ArgumentCaptor<JwtInfo> captor = ArgumentCaptor.forClass(JwtInfo.class);

    // When
    when(this.usersPort.findByUsername(username)).thenReturn(user);
    when(this.passwordEncoder.matches(password, user.password())).thenReturn(true);
    when(this.jwtService.generateToken(any())).thenReturn(token);

    final String result = this.userServiceImpl.loginUser(username, password);

    // Then
    assertThat(result).isEqualTo(token);

    verify(this.jwtService).generateToken(captor.capture());
    final JwtInfo jwtInfo = captor.getValue();

    assertThat(jwtInfo).extracting(JwtInfo::userId, JwtInfo::username, JwtInfo::role)
        .containsExactly(user.id(), user.username(), user.role());
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenInexistentUsername_whenLoginUser_thenThrowException(String username, String password) {
    // When
    when(this.usersPort.findByUsername(username)).thenReturn(null);

    // Then
    assertThatThrownBy(() -> this.userServiceImpl.loginUser(username, password))
        .isInstanceOf(UserNotFoundException.class)
        .extracting("description")
        .isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());
  }

}