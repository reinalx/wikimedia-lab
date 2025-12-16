package com.learn.wikimedialab.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import java.util.List;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class EventsServiceImplTest {

  @InjectMocks
  private EventsServiceImpl eventsServiceImpl;

  @Mock
  private WikimediaEventsPort wikimediaEventsAdapter;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenWikimediaEvent_whenProcessEvent_thenInvokeSaveFilteredEvent(WikimediaEvent event) {
    // When
    this.eventsServiceImpl.processEvent(event);

    // Then
    verify(this.wikimediaEventsAdapter, times(1)).saveFilteredEvent(event);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenPageAndSize_whenGetWikimediaEvents_thenReturnWikimediaEvents(int page, int size,
      List<WikimediaEvent> wikimediaEvents) {
    // When
    when(this.wikimediaEventsAdapter.getWikimediaEvents(page, size)).thenReturn(wikimediaEvents);

    final List<WikimediaEvent> result = this.eventsServiceImpl.getWikimediaEvents(page, size);

    // Then
    assertThat(result).isEqualTo(wikimediaEvents);
  }
}