package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.wikimedia.avro.WikimediaAnalysisEvent;
import com.wikimedia.avro.WikimediaFilteredEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KafkaEventsMapper {

  List<WikimediaAnalysisEvent> toWikimediaAnalysisEvent(List<EventAnalysis> eventAnalysisList);

  @Mapping(target = "analysisId", source = "id")
  @Mapping(target = "createdEventAt", expression = "java(java.time.Instant.now())")
  WikimediaAnalysisEvent toWikimediaAnalysisEvent(EventAnalysis eventAnalysis);

  WikimediaEvent toWikimediaEvent(WikimediaFilteredEvent wikimediaFilteredEvent);

}
