package com.learn.wikimedialab.kafka.adapters.in;

import com.learn.wikimedialab.domain.services.WikimediaProcessorService;
import com.learn.wikimedialab.kafka.mappers.WikimediaRawEventMapper;
import com.wikimedia.avro.WikimediaRawEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * Kafka implementation of the EventConsumerAdapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumerAdapter {

  private final WikimediaProcessorService wikimediaProcessorService;

  private final WikimediaRawEventMapper mapper;


  @Transactional
  @KafkaListener(
      topics = "${app.kafka.topics.raw}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  public void consumer(WikimediaRawEvent event) {
    log.info("Received event: {}", event);
    this.wikimediaProcessorService.processEvent(
        this.mapper.toWikimediaEvent(event)
    );
  }
}
