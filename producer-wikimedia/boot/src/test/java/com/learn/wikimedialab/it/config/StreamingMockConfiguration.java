package com.learn.wikimedialab.it.config;

import com.learn.wikimedialab.domain.adapters.StreamingClientAdapter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

@TestConfiguration
public class StreamingMockConfiguration {

    @Bean
    @Primary
    public StreamingClientAdapter testStreamingClientAdapter() {
        return () -> Flux.just(
                "{\"type\":\"edit\",\"title\":\"Test Event 1\"}",
                "{\"type\":\"edit\",\"title\":\"Test Event 2\"}",
                "INVALID_EVENT"
        );
    }
}