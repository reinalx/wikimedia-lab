package com.learn.wikimedialab.kafka.adapters.in;

import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import com.learn.wikimedialab.kafka.mappers.KafkaEventsMapper;
import com.wikimedia.avro.WikimediaFilteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


/**
 * Kafka implementation of the EventConsumerAdapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumerAdapter {

  private final EventsService wikimediaProcessorService;

  private final KafkaEventsMapper mapper;

  /**
   * Consumes an event from Kafka and processes it.
   *
   * @param event the event data as a String
   */
  @KafkaListener(
      topics = "${app.kafka.topics.filtered}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void consumer(WikimediaFilteredEvent event, Acknowledgment ack) {
    log.info("Received event: {}", event);
    this.wikimediaProcessorService.processEvent(
        this.mapper.toWikimediaEvent(event)
    );
    ack.acknowledge();
  }
}
