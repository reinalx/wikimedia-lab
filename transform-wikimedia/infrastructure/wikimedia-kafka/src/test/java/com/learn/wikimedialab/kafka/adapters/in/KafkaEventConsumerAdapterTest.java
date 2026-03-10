package com.learn.wikimedialab.kafka.adapters.in;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.domain.services.WikimediaProcessorService;
import com.learn.wikimedialab.kafka.mappers.WikimediaRawEventMapper;
import com.wikimedia.avro.WikimediaRawEvent;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaEventConsumerAdapterTest {

  @InjectMocks
  private KafkaEventConsumerAdapter kafkaEventConsumerAdapter;

  @Mock
  private WikimediaProcessorService wikimediaProcessorService;

  @Mock
  private WikimediaRawEventMapper mapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenConsumer_thenProcessEvent(WikimediaEvent wikimediaEvent,
      WikimediaRawEvent event) {
    // Given
    when(this.mapper.toWikimediaEvent(event)).thenReturn(wikimediaEvent);

    // When
    this.kafkaEventConsumerAdapter.consumer(event);

    // Then
    verify(this.wikimediaProcessorService).processEvent(wikimediaEvent);
  }

}