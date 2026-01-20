package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.wikimedia.avro.WikimediaAnalysisEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for Kafka event analysis.
 */
@Mapper(componentModel = "spring")
public interface WikimediaAnalysisEventMapper {


  /**
   * Maps a list of EventAnalysis to a list of EventAnalysisCreated events.
   *
   * @param eventAnalysisList The list of EventAnalysis entities.
   * @return The list of mapped EventAnalysisCreated events.
   */
  List<WikimediaAnalysisEvent> toWikimediaAnalysisEvent(List<EventAnalysis> eventAnalysisList);

  /**
   * Maps EventAnalysis to EventAnalysisCreated event.
   *
   * @param eventAnalysis The EventAnalysis entity.
   * @return The mapped EventAnalysisCreated event.
   */
  @Mapping(target = "analysisId", source = "id")
  @Mapping(target = "createdEventAt", expression = "java(java.time.Instant.now())")
  WikimediaAnalysisEvent toWikimediaAnalysisEvent(EventAnalysis eventAnalysis);
}
