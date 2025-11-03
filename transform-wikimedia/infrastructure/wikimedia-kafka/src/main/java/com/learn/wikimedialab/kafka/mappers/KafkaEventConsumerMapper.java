package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.kafka.utils.JsonToObjectMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for Kafka event consumers.
 */
@Mapper(componentModel = "spring", uses = {JsonToObjectMapper.class})
public interface KafkaEventConsumerMapper {

  /**
   * Converts a JSON string to a WikimediaEvent object.
   *
   * @param eventJson the event data as a JSON string
   * @return the corresponding WikimediaEvent object
   */
  WikimediaEvent toWikimediaEvent(String eventJson);

}
