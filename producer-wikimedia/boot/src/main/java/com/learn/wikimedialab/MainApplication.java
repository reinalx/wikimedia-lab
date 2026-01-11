package com.learn.wikimedialab;

import com.learn.wikimedialab.domain.services.StreamingProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Wikimedia Lab.
 */
@SpringBootApplication
@RequiredArgsConstructor
public class MainApplication {

    private final StreamingProcessingService streamingProcessingService;

    /**
     * Main method to run the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
