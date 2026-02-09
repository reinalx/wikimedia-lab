package com.learn.wikimedialab.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.exceptions.EventNotFoundException;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPort;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.domain.ports.out.idempotence.OutboxPort;
import com.learn.wikimedialab.domain.values.ErrorCode;
import com.learn.wikimedialab.domain.values.OutboxEventType;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class AnalysisServiceImplTest {

  @InjectMocks
  private AnalysisServiceImpl analysisServiceImpl;

  @Mock
  private EventAnalysisPort eventAnalysisPort;

  @Mock
  private OutboxPort outboxPort;

  @Mock
  private WikimediaEventsPort wikimediaEventsPort;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void whenAnalyzeEvent_thenPublishEvent(EventAnalysis eventAnalysis) {
    // Given
    final ArgumentCaptor<Outbox<EventAnalysis>> outboxCaptor = ArgumentCaptor.forClass(
        Outbox.class);
    // When
    when(this.wikimediaEventsPort.existsWikimediaEventById(eventAnalysis.eventId()))
        .thenReturn(true);
    when(this.eventAnalysisPort.publishAnalysisEvent(eventAnalysis))
        .thenReturn(eventAnalysis);

    this.analysisServiceImpl.analyzeEvent(eventAnalysis);
    // Then
    verify(this.outboxPort, times(1)).saveEvent(outboxCaptor.capture());

    final Outbox<EventAnalysis> capturedOutbox = outboxCaptor.getValue();
    assertThat(capturedOutbox).extracting(Outbox::aggregateId, Outbox::aggregateType,
            Outbox::eventType, Outbox::status, Outbox::payload)
        .containsExactly(
            eventAnalysis.id(),
            EventAnalysis.class.getSimpleName(),
            OutboxEventType.CREATE,
            OutboxStatus.PENDING,
            eventAnalysis
        );
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void whenAnalyzeNoExistentEvent_thenThrowsException(EventAnalysis eventAnalysis) {
    // When
    when(this.wikimediaEventsPort.existsWikimediaEventById(eventAnalysis.eventId()))
        .thenReturn(false);

    // Then
    assertThatThrownBy(() -> this.analysisServiceImpl.analyzeEvent(eventAnalysis))
        .isInstanceOf(EventNotFoundException.class)
        .extracting("description")
        .isEqualTo(ErrorCode.EVENT_NOT_FOUND.getMessage());
  }

}