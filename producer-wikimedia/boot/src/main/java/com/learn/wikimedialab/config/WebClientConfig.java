package com.learn.wikimedialab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for WebClient settings.
 */
@Configuration
public class WebClientConfig {
  
  /**
   * The Wikimedia stream URL injected from application properties.
   */
  @Value("${wikimedia.stream.url}")
  private String streamUrl;

  /**
   * Creates a WebClient bean configured with the Wikimedia stream URL.
   *
   * @return a WebClient instance
   */
  @Bean
  public WebClient wikimediaWebClient() {

    return WebClient.create(this.streamUrl);
  }
}
