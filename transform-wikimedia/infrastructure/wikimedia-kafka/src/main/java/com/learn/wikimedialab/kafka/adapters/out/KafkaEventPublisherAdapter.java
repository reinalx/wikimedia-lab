package com.learn.wikimedialab.kafka.adapters.out;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka implementation of the EventPublisherAdapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements EventPublisherAdapter {

  private final KafkaTemplate<String, String> kafkaTemplate;

  /**
   * Publishes an event to Kafka.
   *
   * @param event the event data as a String
   */
  @Override
  public void publish(String event) {
    this.kafkaTemplate.send("wikimedia.filtered.events", event);
    log.info("Published event to Kafka: {}", event);
  }
}
