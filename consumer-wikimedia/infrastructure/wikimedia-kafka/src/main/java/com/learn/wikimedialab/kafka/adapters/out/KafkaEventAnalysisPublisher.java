package com.learn.wikimedialab.kafka.adapters.out;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_EVENT_ANALYSIS_KAFKA_TOPIC;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.events.EventAnalysisCreated;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPublisherPort;
import com.learn.wikimedialab.kafka.mappers.KafkaEventAnalysisMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * Kafka implementation of the EventAnalysisPublisherPort.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventAnalysisPublisher implements EventAnalysisPublisherPort {

  private final KafkaTemplate<String, EventAnalysisCreated> kafkaTemplate;

  private final KafkaEventAnalysisMapper kafkaEventAnalysisMapper;

  /**
   * Publishes a list of event analyses to a Kafka topic.
   *
   * @param events The list of event analyses to be published.
   */
  @Override
  public void publishAnalysisEvent(List<EventAnalysis> events) {
    log.info("Publishing event analysis to Kafka: {}", events);
    final List<EventAnalysisCreated> eventAnalysisCreatedList = this.kafkaEventAnalysisMapper
        .toEventAnalysisCreatedList(events);

    eventAnalysisCreatedList.forEach(eventAnalysisCreated -> {
      this.kafkaTemplate.send(WIKIMEDIA_EVENT_ANALYSIS_KAFKA_TOPIC,
          eventAnalysisCreated.id(), eventAnalysisCreated);
    });
    log.info("Published event analysis to Kafka: {}", eventAnalysisCreatedList);
  }
}
