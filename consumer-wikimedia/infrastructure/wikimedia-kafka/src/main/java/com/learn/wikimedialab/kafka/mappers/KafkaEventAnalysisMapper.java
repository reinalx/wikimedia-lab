package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.events.EventAnalysisCreated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for Kafka event analysis.
 */
@Mapper(componentModel = "spring")
public interface KafkaEventAnalysisMapper {

  /**
   * Maps EventAnalysis to EventAnalysisCreated event.
   *
   * @param eventAnalysis The EventAnalysis entity.
   * @return The mapped EventAnalysisCreated event.
   */
  @Mapping(target = "createdEventAt", expression = "java(java.time.OffsetDateTime.now())")
  EventAnalysisCreated toEventAnalysisCreated(EventAnalysis eventAnalysis);

}
