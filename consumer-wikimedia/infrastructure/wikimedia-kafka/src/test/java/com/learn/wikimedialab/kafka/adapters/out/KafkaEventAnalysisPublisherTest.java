package com.learn.wikimedialab.kafka.adapters.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.kafka.mappers.WikimediaAnalysisEventMapper;
import com.wikimedia.avro.WikimediaAnalysisEvent;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaEventAnalysisPublisherTest {

  @InjectMocks
  private KafkaEventAnalysisPublisher kafkaEventAnalysisPublisher;

  @Mock
  private KafkaTemplate<String, WikimediaAnalysisEvent> kafkaTemplate;

  @Mock
  private WikimediaAnalysisEventMapper mapper;

  @BeforeEach
  void setUp() throws Exception {
    final Field field = KafkaEventAnalysisPublisher.class.getDeclaredField("analyzedTopic");
    field.setAccessible(true);
    field.set(this.kafkaEventAnalysisPublisher, "test-topic");
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEventAnalysisList_whenPublishAnalysisEvent_thenPublishToKafkaAndReturnIds(
      List<EventAnalysis> eventAnalysisList) {
    // Given
    final List<WikimediaAnalysisEvent> eventAnalysisCreatedList = Instancio.ofList(
            WikimediaAnalysisEvent.class)
        .size(eventAnalysisList.size())
        .create();

    final CompletableFuture<SendResult<String, WikimediaAnalysisEvent>> future =
        CompletableFuture.completedFuture(null);

    when(this.mapper.toWikimediaAnalysisEvent(eventAnalysisList))
        .thenReturn(eventAnalysisCreatedList);
    when(this.kafkaTemplate.send(eq("test-topic"), any(String.class),
        any(WikimediaAnalysisEvent.class)))
        .thenReturn(future);

    // When
    final List<String> result = this.kafkaEventAnalysisPublisher.publishAnalysisEvent(
        eventAnalysisList);

    // Then
    verify(this.mapper).toWikimediaAnalysisEvent(eventAnalysisList);
    eventAnalysisCreatedList.forEach(eventAnalysisCreated ->
        verify(this.kafkaTemplate).send("test-topic", eventAnalysisCreated.getAnalysisId(),
            eventAnalysisCreated)
    );

    final List<String> expectedIds = eventAnalysisCreatedList.stream()
        .map(WikimediaAnalysisEvent::getAnalysisId)
        .toList();
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedIds);
  }
}
