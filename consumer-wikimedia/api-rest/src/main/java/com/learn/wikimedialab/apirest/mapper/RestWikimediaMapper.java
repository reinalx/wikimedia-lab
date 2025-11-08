package com.learn.wikimedialab.apirest.mapper;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.model.GetWikimediaEventsResponseDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.model.WikimediaEventDTO;
import com.learn.wikimedialab.domain.entities.events.WikimediaEvent;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for Wikimedia REST API.
 */
@Mapper(componentModel = "spring")
public interface RestWikimediaMapper {

  /**
   * Converts a list of WikimediaEvent domain objects to a GetWikimediaEventsResponseDTO.
   *
   * @param events the WikimediaEvent domain object list.
   * @return the corresponding GetWikimediaEventsResponseDTO
   */
  default GetWikimediaEventsResponseDTO toWikimediaEventsResponse(List<WikimediaEvent> events) {
    final GetWikimediaEventsResponseDTO responseDto = new GetWikimediaEventsResponseDTO();
    responseDto.setEvents(this.toWikimediaEvents(events));
    return responseDto;
  }

  /**
   * Converts a list of WikimediaEvent domain objects to a list of WikimediaEventDTOs.
   *
   * @param wikimediaEvents the WikimediaEvent domain object list.s
   * @return the corresponding WikimediaEventDTO
   */
  List<WikimediaEventDTO> toWikimediaEvents(List<WikimediaEvent> wikimediaEvents);

  /**
   * Converts a list of WikimediaEvent domain objects to a list of WikimediaEventDTO.
   *
   * @param wikimediaEvents the WikimediaEvent domain object list.
   * @return the corresponding list of WikimediaEventDTO
   */
  @Mapping(target = "uri", source = "meta.uri")
  WikimediaEventDTO toWikimediaEvent(WikimediaEvent wikimediaEvents);

}
