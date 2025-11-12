package com.learn.wikimedialab.kafka.adapters.out;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_EVENT_ANALYSIS_KAFKA_TOPIC;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.events.EventAnalysisCreated;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPublisherPort;
import com.learn.wikimedialab.kafka.mappers.KafkaEventAnalysisMapper;
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

  @Override
  public void publishAnalysisEvent(EventAnalysis event) {
    log.info("Publishing event analysis: {}", event);
    final EventAnalysisCreated eventAnalysisCreated =
        this.kafkaEventAnalysisMapper.toEventAnalysisCreated(event);
    this.kafkaTemplate.send(WIKIMEDIA_EVENT_ANALYSIS_KAFKA_TOPIC,
        eventAnalysisCreated.id(), eventAnalysisCreated);
    log.info("Published event analysis to Kafka: {}", eventAnalysisCreated);
  }
}
