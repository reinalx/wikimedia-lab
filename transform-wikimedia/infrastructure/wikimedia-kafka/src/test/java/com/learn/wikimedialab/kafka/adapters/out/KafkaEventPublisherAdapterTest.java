package com.learn.wikimedialab.kafka.adapters.out;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_FILTERED_KAFKA_TOPIC;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.kafka.mappers.WikimediaFilteredEventMapper;
import com.wikimedia.avro.WikimediaFilteredEvent;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaEventPublisherAdapterTest {

  @InjectMocks
  private KafkaEventPublisherAdapter kafkaEventPublisherAdapter;

  @Mock
  private KafkaTemplate<String, WikimediaFilteredEvent> kafkaTemplate;

  @Mock
  private WikimediaFilteredEventMapper mapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenPublish_thenPublishEvent(
      WikimediaEvent event,
      WikimediaFilteredEvent filteredEvent) {
    // Given
    when(this.mapper.toWikimediaFilteredEvent(event)).thenReturn(filteredEvent);

    // When
    this.kafkaEventPublisherAdapter.publish(event);

    // Then
    verify(this.kafkaTemplate).send(WIKIMEDIA_FILTERED_KAFKA_TOPIC, event.id(), filteredEvent);
  }

}