package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.events.EventAnalysisCreated;
import java.util.List;
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

  /**
   * Maps a list of EventAnalysis to a list of EventAnalysisCreated events.
   *
   * @param eventAnalysisList The list of EventAnalysis entities.
   * @return The list of mapped EventAnalysisCreated events.
   */
  List<EventAnalysisCreated> toEventAnalysisCreatedList(List<EventAnalysis> eventAnalysisList);

}
