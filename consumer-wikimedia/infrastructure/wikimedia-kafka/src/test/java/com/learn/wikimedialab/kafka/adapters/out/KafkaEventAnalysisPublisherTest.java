package com.learn.wikimedialab.kafka.adapters.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.events.EventAnalysisCreated;
import com.learn.wikimedialab.kafka.mappers.KafkaEventAnalysisMapper;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaEventAnalysisPublisherTest {

  @InjectMocks
  private KafkaEventAnalysisPublisher kafkaEventAnalysisPublisher;

  @Mock
  private KafkaTemplate<String, EventAnalysisCreated> kafkaTemplate;

  @Mock
  private KafkaEventAnalysisMapper mapper;

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
    final List<EventAnalysisCreated> eventAnalysisCreatedList = Instancio.ofList(
            EventAnalysisCreated.class)
        .size(eventAnalysisList.size())
        .create();

    final CompletableFuture<SendResult<String, EventAnalysisCreated>> future =
        CompletableFuture.completedFuture(null);

    when(this.mapper.toEventAnalysisCreatedList(eventAnalysisList))
        .thenReturn(eventAnalysisCreatedList);
    when(this.kafkaTemplate.send(eq("test-topic"), any(String.class),
        any(EventAnalysisCreated.class)))
        .thenReturn(future);

    // When
    final List<String> result = this.kafkaEventAnalysisPublisher.publishAnalysisEvent(
        eventAnalysisList);

    // Then
    verify(this.mapper).toEventAnalysisCreatedList(eventAnalysisList);
    eventAnalysisCreatedList.forEach(eventAnalysisCreated ->
        verify(this.kafkaTemplate).send("test-topic", eventAnalysisCreated.id(),
            eventAnalysisCreated)
    );

    final List<String> expectedIds = eventAnalysisCreatedList.stream()
        .map(EventAnalysisCreated::id)
        .toList();
    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedIds);
  }
}
