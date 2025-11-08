package com.learn.wikimedialab.apirest.controller;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.EventsApi;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.model.GetWikimediaEventsResponseDTO;
import com.learn.wikimedialab.apirest.mapper.RestWikimediaMapper;
import com.learn.wikimedialab.domain.entities.events.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling Wikimedia events API requests.
 */
@RestController
@RequiredArgsConstructor
public class WikimediaEventsApiController implements EventsApi {

  private final EventsService wikimediaEventsService;

  private final RestWikimediaMapper wikimediaMapper;

  /**
   * Retrieves Wikimedia events with pagination.
   *
   * @param page the page number
   * @param size the number of items per page
   * @return a ResponseEntity containing the Wikimedia events response DTO
   */
  @Override
  public ResponseEntity<GetWikimediaEventsResponseDTO> getWikimediaEvents(Integer page,
      Integer size) {
    final List<WikimediaEvent> events = this.wikimediaEventsService.getWikimediaEvents(page, size);
    return ResponseEntity.ok(
        this.wikimediaMapper.toWikimediaEventsResponse(events)
    );
  }
}
