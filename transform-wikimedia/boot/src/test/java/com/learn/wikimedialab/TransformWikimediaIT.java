package com.learn.wikimedialab;

import static org.assertj.core.api.Assertions.assertThat;

import com.learn.wikimedialab.config.TestContainersConfiguration;
import com.learn.wikimedialab.domain.events.MetaInfo;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.kafka.mappers.JsonToObjectMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

@SpringBootTest
@ActiveProfiles("it")
@Import({TestContainersConfiguration.class, InstancioExtension.class})
public class TransformWikimediaIT {

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  KafkaContainer kafkaContainer;

  @Autowired
  JsonToObjectMapper mapper;

  @Value("${app.kafka.topics.raw}")
  private String rawTopic;

  @Value("${app.kafka.topics.filtered}")
  private String filteredTopic;


  @Test
  void givenRawEvents_whenFiltered_thenForwardOnlyValidEvents() throws Exception {
    // Given

    final List<WikimediaEvent> events = List.of(
        this.validEvent("1", false, "edit", "es.wikipedia.org"),
        this.validEvent("2", false, "edit", "es.wikipedia.org"),
        this.validEvent("3", true, "edit", "es.wikipedia.org"),
        this.validEvent("4", false, "log", "es.wikipedia.org"),
        this.validEvent("5", false, "edit", "en.wikipedia.org")
    );

    // When
    for (final WikimediaEvent event : events) {
      this.kafkaTemplate.send(this.rawTopic, this.mapper.convertEventToJsonString(event));
    }

    this.kafkaTemplate.flush();

    // Then
    final Consumer<String, String> consumer =
        this.createTestConsumer(this.kafkaContainer.getBootstrapServers());

    consumer.subscribe(List.of(this.filteredTopic));

    final List<ConsumerRecord<String, String>> received = new ArrayList<>();

    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> {
          final ConsumerRecords<String, String> records =
              consumer.poll(Duration.ofMillis(500));

          records.forEach(received::add);
          assertThat(received).hasSize(2);
        });

    final List<WikimediaEvent> filteredEvents = received.stream()
        .map(r -> this.mapper.convertJsonStringToEvent(r.value()))
        .toList();

    assertThat(filteredEvents)
        .allMatch(e ->
            e.type().equalsIgnoreCase("edit")
                && !e.bot()
                && e.meta().domain().contains("es.wikipedia.org")
        );

    consumer.close();
  }

  private WikimediaEvent validEvent(
      String id,
      boolean bot,
      String type,
      String domain
  ) {
    return new WikimediaEvent(
        id,
        type,
        "user-" + id,
        "title",
        "url",
        "comment",
        bot,
        new MetaInfo(domain, "uri", "dt")
    );
  }

  private Consumer<String, String> createTestConsumer(String bootstrapServers) {
    final Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "filtered-it-consumer");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new KafkaConsumer<>(props);
  }
}
