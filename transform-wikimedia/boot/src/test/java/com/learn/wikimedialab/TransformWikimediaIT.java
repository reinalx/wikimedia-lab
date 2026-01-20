package com.learn.wikimedialab;

import static com.learn.wikimedialab.utils.WikimediaEventsTestUtils.validEvent;
import static org.assertj.core.api.Assertions.assertThat;

import com.learn.wikimedialab.config.TestContainersConfiguration;
import com.wikimedia.avro.WikimediaFilteredEvent;
import com.wikimedia.avro.WikimediaRawEvent;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.kafka.KafkaContainer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootTest
@ActiveProfiles("it")
@Import({TestContainersConfiguration.class, InstancioExtension.class})
public class TransformWikimediaIT {

  @Autowired
  KafkaTemplate<String, WikimediaRawEvent> kafkaTemplate;

  @Autowired
  KafkaContainer kafkaContainer;

  @Value("${app.kafka.topics.raw}")
  private String rawTopic;

  @Value("${app.kafka.topics.filtered}")
  private String filteredTopic;

  @Value("${spring.kafka.properties.schema.registry.url}")
  private String schemaRegistryUrl;


  @Test
  void givenRawEvents_whenFiltered_thenForwardOnlyValidEvents() {
    // Given
    final List<WikimediaRawEvent> events = List.of(
        validEvent("1", false, "edit", "es.wikipedia.org"),
        validEvent("2", false, "edit", "es.wikipedia.org"),
        validEvent("3", true, "edit", "es.wikipedia.org"),
        validEvent("4", false, "log", "es.wikipedia.org"),
        validEvent("5", false, "edit", "en.wikipedia.org")
    );

    // When
    for (final WikimediaRawEvent event : events) {
      this.kafkaTemplate.send(this.rawTopic, event);
    }

    this.kafkaTemplate.flush();

    final Consumer<String, WikimediaFilteredEvent> consumer =
        this.createTestConsumer(this.kafkaContainer.getBootstrapServers());

    consumer.subscribe(List.of(this.filteredTopic));

    // Then
    final List<ConsumerRecord<String, WikimediaFilteredEvent>> received = new ArrayList<>();

    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> {
          final ConsumerRecords<String, WikimediaFilteredEvent> records =
              consumer.poll(Duration.ofMillis(500));

          records.forEach(received::add);
          assertThat(received).hasSize(2);
        });

    final List<WikimediaFilteredEvent> filteredEvents = received.stream()
        .map(ConsumerRecord::value)
        .toList();

    assertThat(filteredEvents)
        .allMatch(e ->
            e.getType().equalsIgnoreCase("edit")
                && !e.getBot()
                && e.getMeta().getDomain().contains("es.wikipedia.org")
        );

    consumer.close();
  }


  private Consumer<String, WikimediaFilteredEvent> createTestConsumer(String bootstrapServers) {
    final Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "filtered-it-consumer");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, this.schemaRegistryUrl);
    props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

    return new KafkaConsumer<>(props);
  }
}
