package com.learn.wikimedialab.kafka.adapters.out;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_FILTERED_KAFKA_TOPIC;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.kafka.mappers.JsonToObjectMapper;
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

  private final JsonToObjectMapper jsonToObjectMapper;

  /**
   * Publishes an event to Kafka.
   *
   * @param event the event data as a String
   */
  @Override
  public void publish(WikimediaEvent event) {
    final String rawEvent = this.jsonToObjectMapper.convertEventToJsonString(event);
    this.kafkaTemplate.send(WIKIMEDIA_FILTERED_KAFKA_TOPIC, rawEvent);
    log.info("Published event to Kafka: {}", event);
  }
}
