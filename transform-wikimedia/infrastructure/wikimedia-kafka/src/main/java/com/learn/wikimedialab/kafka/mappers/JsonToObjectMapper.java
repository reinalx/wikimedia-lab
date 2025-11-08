package com.learn.wikimedialab.kafka.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 * Utility class to convert JSON strings to WikimediaEvent objects.
 */
@Component
public class JsonToObjectMapper {

  private final ObjectMapper objectMapper;

  /**
   * Constructor for JsonToObjectMapper.
   *
   * @param objectMapper the ObjectMapper instance to use for conversion
   */
  public JsonToObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Converts a JSON string to a WikimediaEvent object.
   *
   * @param jsonString the event data as a JSON string
   * @return the corresponding WikimediaEvent object
   */
  public WikimediaEvent convertJsonStringToEvent(String jsonString) {
    try {
      return this.objectMapper.readValue(jsonString, WikimediaEvent.class);
    } catch (final IOException e) {
      throw new RuntimeException("Error deserializing JSON to WikimediaEvent", e);
    }
  }

  /**
   * Converts a WikimediaEvent object to a JSON string.
   *
   * @param event the WikimediaEvent object to convert
   * @return the corresponding JSON string
   */
  public String convertEventToJsonString(WikimediaEvent event) {
    try {
      return this.objectMapper.writeValueAsString(event);
    } catch (final IOException e) {
      throw new RuntimeException("Error serializing WikimediaEvent to JSON", e);
    }
  }
}
