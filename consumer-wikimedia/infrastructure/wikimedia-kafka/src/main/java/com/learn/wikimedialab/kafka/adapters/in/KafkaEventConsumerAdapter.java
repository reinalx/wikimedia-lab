package com.learn.wikimedialab.kafka.adapters.in;

import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import com.learn.wikimedialab.domain.ports.out.EventConsumerPort;
import com.learn.wikimedialab.kafka.mappers.JsonToObjectMapper;
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
public class KafkaEventConsumerAdapter implements EventConsumerPort {

  private final EventsService wikimediaProcessorService;

  private final JsonToObjectMapper mapper;

  /**
   * Consumes an event from Kafka and processes it.
   *
   * @param event the event data as a String
   */
  @Override
  @KafkaListener(
      topics = "${app.kafka.topics.filtered}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void consumer(String event) {
    log.info("Received event: {}", event);
    try {
      this.wikimediaProcessorService.processEvent(
          this.mapper.convertJsonStringToEvent(event)
      );
    } catch (final Exception e) {
      log.error("Error processing event: {}", e.getMessage());
    }
  }
}
