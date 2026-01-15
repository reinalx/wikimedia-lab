package com.learn.wikimedialab;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.events.EventAnalysisCreated;
import com.learn.wikimedialab.domain.ports.in.services.OutboxService;
import com.learn.wikimedialab.domain.ports.out.OutboxPort;
import com.learn.wikimedialab.domain.values.OutboxEventType;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.instancio.TypeToken;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

@SpringBootTest
@ActiveProfiles("it")
@Import({TestcontainersConfiguration.class, InstancioExtension.class})
class OutboxSchedulerIT {

  @Autowired
  OutboxPort outboxPort;

  @Autowired
  OutboxService outboxService;

  @Autowired
  KafkaContainer kafkaContainer;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenPendingOutbox_whenProcessPendingEvents_thenPublishesAnalysisEventAndMarksOutboxAsProcessed(
      EventAnalysis eventAnalysis) {

    // Given

    final Outbox<EventAnalysis> outbox = org.instancio.Instancio.of(
            new TypeToken<Outbox<EventAnalysis>>() {
            })
        .set(field(Outbox.class, "eventType"), OutboxEventType.CREATE)
        .set(field(Outbox.class, "aggregateId"), eventAnalysis.id())
        .set(field(Outbox.class, "aggregateType"), EventAnalysis.class.getSimpleName())
        .set(field(Outbox.class, "payload"), eventAnalysis)
        .set(field(Outbox.class, "status"), OutboxStatus.PENDING)
        .create();

    this.outboxPort.saveEvent(outbox);

    // When
    this.outboxService.processPendingEvents();

    // Then
    final Consumer<String, EventAnalysisCreated> consumer =
        this.createAnalysisConsumer(this.kafkaContainer.getBootstrapServers());

    consumer.subscribe(List.of("wikimedia.analysis.events"));

    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> {
          final ConsumerRecords<String, EventAnalysisCreated> records =
              consumer.poll(Duration.ofMillis(500));

          assertThat(records.count()).isEqualTo(1);
        });

    consumer.close();
  }

  private Consumer<String, EventAnalysisCreated> createAnalysisConsumer(String bootstrapServers) {

    final Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "analysis-it-" + UUID.randomUUID());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    return new KafkaConsumer<>(props);
  }

}