package com.learn.wikimedialab.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka settings.
 */
@Configuration
public class KafkaConfig {

  /**
   * Creates the Kafka topic for raw Wikimedia events.
   *
   * @return a NewTopic instance for raw events
   */
  @Bean
  public NewTopic filteredEventsTopic() {
    return TopicBuilder.name("wikimedia.filtered.events").partitions(1).replicas(1).build();
  }

}
