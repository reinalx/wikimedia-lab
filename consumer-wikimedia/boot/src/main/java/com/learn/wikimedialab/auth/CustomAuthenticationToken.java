package com.learn.wikimedialab.auth;

import java.io.Serial;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Custom authentication token for handling authentication in the application.
 */
@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String serviceToken;

  private UserDetails userDetails;

  /**
   * Constructor for creating an unauthenticated token with a service token.
   *
   * @param authorities  the granted authorities
   * @param serviceToken the service token
   */
  public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
      final String serviceToken) {
    super(authorities);
    this.serviceToken = serviceToken;
  }

  /**
   * Constructor for creating an authenticated token with user details.
   *
   * @param authorities     the granted authorities
   * @param userDetails     the user details
   * @param serviceToken    the service token
   * @param isAuthenticated whether the token is authenticated
   */
  public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
      final UserDetails userDetails, final String serviceToken, final boolean isAuthenticated) {
    super(authorities);
    this.userDetails = userDetails;
    this.serviceToken = serviceToken;
    this.setAuthenticated(isAuthenticated);
  }

  /**
   * Gets the credentials (service token) associated with this token.
   *
   * @return the service token
   */
  @Override
  public Object getCredentials() {
    return this.serviceToken;
  }

  /**
   * Gets the principal (user details) associated with this token.
   *
   * @return the user details
   */
  @Override
  public Object getPrincipal() {
    return this.userDetails;
  }
}
