package com.learn.wikimedialab.auth;

import com.learn.wikimedialab.domain.entities.JwtInfo;
import com.learn.wikimedialab.domain.exceptions.JwtExpiredException;
import com.learn.wikimedialab.domain.ports.in.services.JwtService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Custom authentication provider for handling JWT authentication.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final JwtService jwtService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String token = (String) authentication.getCredentials();

    if (this.jwtService.isTokenExpired(token)) {
      throw new JwtExpiredException();
    }
    final JwtInfo jwtInfo = this.jwtService.getInfo(token);
    final UserDetails userDetails = UserDetails.builder()
        .userId(jwtInfo.userId())
        .username(jwtInfo.username())
        .role(jwtInfo.role())
        .build();

    final Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + jwtInfo.role()));
    return new CustomAuthenticationToken(authorities, userDetails, token, true);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(CustomAuthenticationToken.class);
  }

}
