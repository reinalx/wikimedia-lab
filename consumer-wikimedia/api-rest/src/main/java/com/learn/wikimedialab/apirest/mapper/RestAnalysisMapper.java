package com.learn.wikimedialab.apirest.mapper;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.model.CreateEventAnalysisRequestDTO;
import com.learn.wikimedialab.domain.entities.events.EventAnalysis;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between REST API DTOs and domain entities related to analysis.
 */
@Mapper(componentModel = "spring")
public interface RestAnalysisMapper {

  /**
   * Converts a CreateEventAnalysisRequestDTO to an EventAnalysis entity.
   *
   * @param createEventAnalysisRequestDTO the DTO to convert
   * @return the corresponding EventAnalysis entity
   */
  EventAnalysis toEventAnalysis(CreateEventAnalysisRequestDTO createEventAnalysisRequestDTO);

}
