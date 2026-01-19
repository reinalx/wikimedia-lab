package com.learn.wikimedialab.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.entities.EventAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

/**
 * Utility class for serializing payloads to JSON.
 */
@Component
@RequiredArgsConstructor
public class PayloadSerializer {

  private final ObjectMapper objectMapper;

  /**
   * Converts a payload object to an EventAnalysis instance.
   *
   * @param payload The payload object to convert.
   * @return The converted EventAnalysis instance.
   */
  @SneakyThrows
  @Named("deserializePayloadToEventAnalysis")
  public EventAnalysis toEventAnalysis(Object payload) {
    return this.objectMapper.convertValue(payload, EventAnalysis.class);
  }
}

