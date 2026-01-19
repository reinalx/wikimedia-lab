package com.learn.wikimedialab.domain.ports.in.services;

import com.learn.wikimedialab.domain.entities.auth.JwtInfo;

/**
 * Service interface for JWT operations.
 */
public interface JwtService {

  /**
   * Generates a JWT token based on the provided JwtInfo.
   *
   * @param jwtInfo The JwtInfo containing user details.
   * @return The generated JWT token as a String.
   */
  String generateToken(JwtInfo jwtInfo);

  /**
   * Extracts JwtInfo from the provided JWT token.
   *
   * @param token The JWT token.
   * @return The extracted JwtInfo.
   */
  JwtInfo getInfo(String token);
  
}
