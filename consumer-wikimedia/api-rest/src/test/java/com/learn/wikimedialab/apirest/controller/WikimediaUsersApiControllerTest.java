package com.learn.wikimedialab.apirest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserRequestDTO;
import com.learn.wikimedialab.domain.ports.in.services.UsersService;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WikimediaUsersApiControllerTest {

  @InjectMocks
  private WikimediaUsersApiController wikimediaUsersApiController;

  @Mock
  private UsersService usersService;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenLoginRequest_whenLoginUser_thenReturnUserData(LoginUserRequestDTO loginUserRequestDTO,
      String token) {
    // When
    when(this.usersService.loginUser(
        loginUserRequestDTO.getUsername(),
        loginUserRequestDTO.getPassword()
    )).thenReturn(token);

    final var response = this.wikimediaUsersApiController
        .loginUser(loginUserRequestDTO);

    // Then
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getToken()).isEqualTo(token);
  }

}