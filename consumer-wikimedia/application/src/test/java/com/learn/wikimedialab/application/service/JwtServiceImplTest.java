package com.learn.wikimedialab.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.learn.wikimedialab.domain.entities.auth.JwtInfo;
import com.learn.wikimedialab.domain.exceptions.JwtExpiredException;
import java.util.Date;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class JwtServiceImplTest {

  private static final String TEST_SECRET_KEY = "testSecretKeyForJwtTokenGenerationAndValidationPurposesOnlyWith64Characters!";

  @InjectMocks
  private JwtServiceImpl jwtServiceImpl;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(this.jwtServiceImpl, "secretKey", TEST_SECRET_KEY);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenJwtInfo_whenGenerateToken_thenReturnValidToken(JwtInfo jwtInfo) {
    // When
    final String token = this.jwtServiceImpl.generateToken(jwtInfo);

    // Then
    assertThat(token).isNotNull().isNotEmpty();
    assertThat(token.split("\\.")).hasSize(3);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenValidToken_whenGetInfo_thenReturnJwtInfo(JwtInfo jwtInfo) {
    // Given
    final String token = this.jwtServiceImpl.generateToken(jwtInfo);

    // When
    final JwtInfo result = this.jwtServiceImpl.getInfo(token);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo(jwtInfo.username());
    assertThat(result.userId()).isEqualTo(jwtInfo.userId());
    assertThat(result.role()).isEqualTo(jwtInfo.role());
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenExpiredToken_whenGetInfo_thenThrowJwtExpiredException(JwtInfo jwtInfo) {
    // Given
    ReflectionTestUtils.setField(this.jwtServiceImpl, "secretKey", TEST_SECRET_KEY);
    final String token = this.jwtServiceImpl.generateToken(jwtInfo);

    try {
      Thread.sleep(100);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    final String expiredToken = io.jsonwebtoken.Jwts.builder()
        .setSubject(jwtInfo.username())
        .setExpiration(new Date(System.currentTimeMillis() - 10000))
        .claim("userId", jwtInfo.userId())
        .claim("role", jwtInfo.role())
        .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(TEST_SECRET_KEY.getBytes()),
            io.jsonwebtoken.SignatureAlgorithm.HS512)
        .compact();

    // When & Then
    assertThatThrownBy(() -> this.jwtServiceImpl.getInfo(expiredToken))
        .isInstanceOf(JwtExpiredException.class);
  }
}
