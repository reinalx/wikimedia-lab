package com.learn.wikimedialab.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPublisherPort;
import com.learn.wikimedialab.domain.ports.out.OutboxPort;
import java.util.List;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class OutboxServiceImplTest {

  @InjectMocks
  private OutboxServiceImpl outboxServiceImpl;

  @Mock
  private OutboxPort outboxPort;

  @Mock
  private EventAnalysisPublisherPort eventAnalysisPublisherPort;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void whenProcessPendingEvents_thenSuccess(List<Outbox<EventAnalysis>> outboxes) {
    // Given
    final List<String> publishedIds = outboxes.stream()
        .map(outbox -> outbox.payload().id())
        .toList();
    final ArgumentCaptor<List<Outbox<EventAnalysis>>> captor = ArgumentCaptor.forClass(List.class);
    // When
    when(this.outboxPort.fetchPendingEvents()).thenReturn(outboxes);
    when(this.eventAnalysisPublisherPort.publishAnalysisEvent(anyList())).thenReturn(publishedIds);

    this.outboxServiceImpl.processPendingEvents();

    // Then
    verify(this.outboxPort).updateOutboxStatusToProcessed(captor.capture());
    final List<Outbox<EventAnalysis>> processedOutboxes = captor.getValue();
    assertThat(processedOutboxes).isEqualTo(outboxes);

  }
}