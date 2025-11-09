package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.auth.JwtInfo;
import com.learn.wikimedialab.domain.ports.in.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
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

  /**
   * Extracts JWT information from the token.
   *
   * @param token The JWT token.
   * @return The extracted JwtInfo.
   */
  @Override
  public JwtInfo getInfo(final String token) {
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
  }

  /**
   * Checks if the JWT token is expired.
   *
   * @param token The JWT token.
   * @return true if the token is expired, false otherwise.
   */
  @Override
  public Boolean isTokenExpired(final String token) {
    return this.extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date from the JWT token.
   *
   * @param token The JWT token.
   * @return The expiration date.
   */
  private Date extractExpiration(final String token) {
    return this.extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts a specific claim from the JWT token.
   *
   * @param token         The JWT token.
   * @param claimResolver A function to extract the desired claim.
   * @param <T>           The type of the claim.
   * @return The extracted claim.
   */
  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = this.extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  /**
   * Extracts all claims from the JWT token.
   *
   * @param token The JWT token.
   * @return The claims contained in the token.
   */
  private Claims extractAllClaims(final String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
        .build().parseClaimsJws(token)
        .getBody();
  }
}
