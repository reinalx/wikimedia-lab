package com.learn.wikimedialab.kafka.adapters.out;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_FILTERED_KAFKA_TOPIC;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.kafka.mappers.JsonToObjectMapper;
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
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private JsonToObjectMapper jsonToObjectMapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenPublish_thenPublishEvent(WikimediaEvent event, String rawEvent) {
    // When
    when(this.jsonToObjectMapper.convertEventToJsonString(event)).thenReturn(rawEvent);
    this.kafkaEventPublisherAdapter.publish(event);

    // Then
    verify(this.kafkaTemplate).send(WIKIMEDIA_FILTERED_KAFKA_TOPIC, rawEvent);
  }

}