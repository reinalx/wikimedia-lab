package com.learn.wikimedialab.kafka.adapters;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_RAW_KAFKA_TOPIC;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Implementation of the KafkaEventPublisherAdapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapterImpl implements EventPublisherAdapter {

  private final KafkaTemplate<String, String> kafkaTemplate;

  /**
   * Publishes an event to the Kafka topic "wikimedia.raw.events".
   *
   * @param event the event data as a String
   */
  @Override
  public void publishEvent(String event) {
    this.kafkaTemplate.send(WIKIMEDIA_RAW_KAFKA_TOPIC, event);
    log.info("Event sent to Kafka {}", event.substring(0, Math.min(event.length(), 200)));
  }

}
