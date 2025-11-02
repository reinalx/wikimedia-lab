package com.learn.wikimedialab;

import com.learn.wikimedialab.domain.services.StreamingProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Wikimedia Lab.
 */
@SpringBootApplication
@RequiredArgsConstructor
public class MainApplication implements CommandLineRunner {

  private final StreamingProcessingService streamingProcessingService;

  /**
   * Main method to run the Spring Boot application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }


  /**
   * Runs the application and starts the streaming processing service.
   *
   * @param args command-line arguments
   */
  @Override
  public void run(String... args) {
    this.streamingProcessingService.startStreaming();
  }
}
