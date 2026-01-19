package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.auth.JwtInfo;
import com.learn.wikimedialab.domain.exceptions.JwtExpiredException;
import com.learn.wikimedialab.domain.ports.in.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation of JwtService for handling JWT operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private static final int JWT_EXPIRATION_MS = 1440; //

  @Value("${app.auth.secret.key}")
  private String secretKey;

  @Override
  public String generateToken(final JwtInfo jwtInfo) {

    final Claims claims = Jwts.claims();
    claims.setSubject(jwtInfo.username())
        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS * 60 * 1000));
    claims.put("userId", jwtInfo.userId());
    claims.put("role", jwtInfo.role());

    return Jwts.builder()
        .setClaims(claims)
        .signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes()), SignatureAlgorithm.HS512)
        .compact();
  }

  @Override
  public JwtInfo getInfo(final String token) {
    try {
      final Claims claims =
          Jwts.parserBuilder()
              .setSigningKey(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
              .build()
              .parseClaimsJws(token)
              .getBody();

      return JwtInfo.builder()
          .username(claims.getSubject())
          .userId(claims.get("userId").toString())
          .role(claims.get("role").toString())
          .build();

    } catch (final ExpiredJwtException expiredJwtException) {
      log.error("JWT token is expired: {}", expiredJwtException.getMessage());
      throw new JwtExpiredException();
    }
  }

}
