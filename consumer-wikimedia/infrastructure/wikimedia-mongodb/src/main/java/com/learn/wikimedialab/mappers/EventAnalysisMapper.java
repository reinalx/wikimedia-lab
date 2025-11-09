package com.learn.wikimedialab.mappers;

import com.learn.wikimedialab.domain.entities.events.EventAnalysis;
import com.learn.wikimedialab.mongodb.entities.EventAnalysisEntity;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between EventAnalysis and EventAnalysisEntity.
 */
@Mapper(componentModel = "spring")
public interface EventAnalysisMapper {


  /**
   * Converts an EventAnalysis to an EventAnalysisEntity.
   *
   * @param eventAnalysis The EventAnalysis to be converted.
   * @return The corresponding EventAnalysisEntity.
   */
  EventAnalysisEntity toEntity(EventAnalysis eventAnalysis);

}
