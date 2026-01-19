package com.learn.wikimedialab.config;

import com.learn.wikimedialab.domain.services.StreamingProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Startup runner that initiates streaming processing on application startup.
 * This component is excluded from the 'it' (integration test) profile.
 */
@Component
@Profile("!it")
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final StreamingProcessingService streamingProcessingService;

    /**
     * Runs the streaming processing service on application startup.
     *
     * @param args command-line arguments
     */
    @Override
    public void run(String... args) {
        this.streamingProcessingService.startStreaming();
    }
}