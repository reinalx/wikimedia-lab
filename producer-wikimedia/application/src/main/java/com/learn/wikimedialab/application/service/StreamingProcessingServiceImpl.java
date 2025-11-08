package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import com.learn.wikimedialab.domain.adapters.StreamingClientAdapter;
import com.learn.wikimedialab.domain.services.StreamingProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for processing streaming data from Wikimedia Lab and sending it to Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamingProcessingServiceImpl implements StreamingProcessingService {

  private final StreamingClientAdapter streamingClientAdapter;

  private final EventPublisherAdapter eventPublisherAdapter;

  /**
   * Starts streaming events from Wikimedia and publishes them to Kafka.
   */
  @Override
  public void startStreaming() {
    this.streamingClientAdapter.streamEvents()
        .filter(event -> event != null && event.trim().startsWith("{"))
        .subscribe(
            this.eventPublisherAdapter::publishEvent,
            error -> log.error("Error streaming events: {}", error.getMessage())
        );
  }
}
