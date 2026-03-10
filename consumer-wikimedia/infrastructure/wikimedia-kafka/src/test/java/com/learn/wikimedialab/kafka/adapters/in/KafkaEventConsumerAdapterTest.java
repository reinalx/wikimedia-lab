package com.learn.wikimedialab.kafka.adapters.in;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.in.services.EventsService;
import com.learn.wikimedialab.kafka.mappers.KafkaEventsMapper;
import com.wikimedia.avro.WikimediaFilteredEvent;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaEventConsumerAdapterTest {

  @InjectMocks
  private KafkaEventConsumerAdapter kafkaEventConsumerAdapter;

  @Mock
  private EventsService eventsService;

  @Mock
  private KafkaEventsMapper mapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenConsumeEvent_thenProcessEvent(WikimediaFilteredEvent event,
      WikimediaEvent wikimediaEvent) {
    // Given
    final Acknowledgment ack = mock(Acknowledgment.class);

    when(this.mapper.toWikimediaEvent(event))
        .thenReturn(wikimediaEvent);

    // When

    this.kafkaEventConsumerAdapter.consumer(event, ack);

    // Then
    verify(this.eventsService).processEvent(wikimediaEvent);
    verify(ack, times(1)).acknowledge();
  }
}