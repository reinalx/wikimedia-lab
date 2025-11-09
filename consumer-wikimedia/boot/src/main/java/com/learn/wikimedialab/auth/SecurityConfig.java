package com.learn.wikimedialab.auth;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationProvider authenticationProvider;

  /**
   * Security filter chain configuration.
   *
   * @param http                  HttpSecurity instance
   * @param authenticationManager AuthenticationManager instance
   * @return SecurityFilterChain
   */
  @Bean
  @SneakyThrows
  protected SecurityFilterChain filterChain(HttpSecurity http,
      AuthenticationManager authenticationManager) {

    http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(httpSecuritySessionManagementConfigurer ->
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("v1/auth/login").permitAll()
            .requestMatchers("v1/analysis").hasRole("EDITOR")
            .requestMatchers("v1/events").authenticated()
        )
        .addFilterBefore(new JwtFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class);
    return http.build();

  }


  /**
   * Authentication manager bean configuration.
   *
   * @param http HttpSecurity instance
   * @return AuthenticationManager
   * @throws Exception if an error occurs
   */
  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    final AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(this.authenticationProvider);
    return authenticationManagerBuilder.build();
  }


}
