package com.learn.wikimedialab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Wikimedia Lab.
 */
@SpringBootApplication(scanBasePackages = "com.learn.wikimedialab")
public class MainApplication {

  /**
   * Main method to run the Spring Boot application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

}
