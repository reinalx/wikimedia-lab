package com.learn.wikimedialab.apirest.controller;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.UsersApi;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserRequestDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserResponseDTO;
import com.learn.wikimedialab.domain.ports.in.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling Wikimedia users API requests.
 */
@RestController
@RequiredArgsConstructor
public class WikimediaUsersApiController implements UsersApi {

  private final UsersService usersService;

  /**
   * Logs in a user.
   *
   * @param loginUserRequestDto the login user request DTO
   * @return a ResponseEntity containing the login user response DTO
   */
  @Override
  public ResponseEntity<LoginUserResponseDTO> loginUser(LoginUserRequestDTO loginUserRequestDto) {
    final String token = this.usersService.loginUser(
        loginUserRequestDto.getUsername(),
        loginUserRequestDto.getPassword()
    );
    return ResponseEntity.ok(
        new LoginUserResponseDTO().token(token)
    );
  }
}
