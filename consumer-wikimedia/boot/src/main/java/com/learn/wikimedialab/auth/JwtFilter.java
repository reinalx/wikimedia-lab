package com.learn.wikimedialab.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Filter for processing incoming requests and handling authentication.
 */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  /**
   * Determines if the filter should not be applied to the request.
   *
   * @param request the HTTP request
   * @return true if the filter should not be applied, false otherwise
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    final String path = request.getServletPath();
    return path.startsWith("/assets/") || path.startsWith("/images/") || path.startsWith(
        "/static/");
  }

  /**
   * Filters incoming requests to authenticate using JWT tokens.
   *
   * @param request     the HTTP request
   * @param response    the HTTP response
   * @param filterChain the filter chain
   * @throws ServletException if a servlet error occurs
   * @throws IOException      if an I/O error occurs
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeaderValue == null || !authHeaderValue.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      final String serviceToken = authHeaderValue.replace("Bearer ", "");
      this.configureSecurityContext(serviceToken);
    } catch (final Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Configures the security context with the authenticated user.
   *
   * @param serviceToken the service token
   */
  private void configureSecurityContext(String serviceToken) {
    final Authentication authentication = this.authenticationManager.authenticate(
        new CustomAuthenticationToken(
            List.of(), serviceToken));
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
