package com.learn.wikimedialab.kafka.adapters.in;

import com.learn.wikimedialab.domain.adapters.EventConsumerAdapter;
import com.learn.wikimedialab.domain.services.WikimediaProcessorService;
import com.learn.wikimedialab.kafka.mappers.KafkaEventConsumerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * Kafka implementation of the EventConsumerAdapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumerAdapter implements EventConsumerAdapter {

  private final WikimediaProcessorService wikimediaProcessorService;

  private final KafkaEventConsumerMapper kafkaEventConsumerMapper;

  /**
   * Consumes an event from Kafka and processes it.
   *
   * @param event the event data as a String
   */
  @Override
  @KafkaListener(
      topics = "${app.kafka.topics.raw}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void consumer(String event) {
    log.info("Received event: {}", event);
    try {
      this.wikimediaProcessorService.processEvent(
          this.kafkaEventConsumerMapper.toWikimediaEvent(event)
      );
    } catch (final Exception e) {
      log.error("Error processing event: {}", e.getMessage());
    }
  }
}
