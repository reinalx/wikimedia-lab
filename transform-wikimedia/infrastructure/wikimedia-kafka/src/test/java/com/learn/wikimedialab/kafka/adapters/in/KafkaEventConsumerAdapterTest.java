package com.learn.wikimedialab.kafka.adapters.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.domain.services.WikimediaProcessorService;
import com.learn.wikimedialab.kafka.mappers.JsonToObjectMapper;
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
  private JsonToObjectMapper mapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenConsumer_thenProcessEvent(WikimediaEvent wikimediaEvent, String event) {
    // When
    when(this.mapper.convertJsonStringToEvent(event)).thenReturn(wikimediaEvent);
    this.kafkaEventConsumerAdapter.consumer(event);

    // Then
    verify(this.wikimediaProcessorService).processEvent(wikimediaEvent);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenError_whenConsumer_thenNotProcessEvent(String event) {
    // When
    when(this.mapper.convertJsonStringToEvent(event)).thenThrow(
        new RuntimeException("Mapping error"));

    this.kafkaEventConsumerAdapter.consumer(event);
    // Then
    verify(this.wikimediaProcessorService, times(0)).processEvent(any());
  }

}