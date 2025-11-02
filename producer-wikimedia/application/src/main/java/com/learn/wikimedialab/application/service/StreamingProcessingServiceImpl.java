package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.services.StreamingProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for processing streaming data from Wikimedia Lab and sending it to Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamingProcessingServiceImpl implements StreamingProcessingService {

  private final WebClient webClient;
  private final KafkaTemplate<String, String> kafkaTemplate;

  /**
   * Starts streaming data from Wikimedia Lab and sends it to Kafka.
   */
  @Override
  public void startStreaming() {
    this.webClient.get()
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
        })
        .filter(event -> event != null && event.data() != null)
        .map(ServerSentEvent::data)
        .filter(data -> data.trim().startsWith("{"))
        .subscribe(
            data -> {
              this.kafkaTemplate.send("wikimedia.raw.events", data);
              log.info("Event send to kafka {}",
                  data.substring(0, Math.min(data.length(), 200)));
            },
            error -> log.error("❌ Error en el stream: {}", error.getMessage(), error),
            () -> log.info("Completed streaming from Wikimedia Lab.")
        );
  }
}
