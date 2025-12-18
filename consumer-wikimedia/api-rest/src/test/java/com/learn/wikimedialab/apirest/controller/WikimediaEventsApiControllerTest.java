package com.learn.wikimedialab.apirest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.model.GetWikimediaEventsResponseDTO;
import com.learn.wikimedialab.apirest.mapper.RestWikimediaMapper;
import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import java.util.List;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WikimediaEventsApiControllerTest {

  @InjectMocks
  private WikimediaEventsApiController wikimediaEventsApiController;

  @Mock
  private EventsService eventsService;

  @Mock
  private RestWikimediaMapper restWikimediaMapper;


  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenParameters_whenGetWikimediaEvents_thenReturnWikimediaEvents(
      Integer page,
      Integer size,
      List<WikimediaEvent> wikimediaEvents,
      GetWikimediaEventsResponseDTO getWikimediaEventsResponseDTO
  ) {
    // When
    when(this.eventsService.getWikimediaEvents(page, size)).thenReturn(wikimediaEvents);
    when(this.restWikimediaMapper.toWikimediaEventsResponse(wikimediaEvents))
        .thenReturn(getWikimediaEventsResponseDTO);

    final var response = this.wikimediaEventsApiController.getWikimediaEvents(page, size);
    // Then
    assertThat(response.getBody()).isEqualTo(getWikimediaEventsResponseDTO);
  }
}