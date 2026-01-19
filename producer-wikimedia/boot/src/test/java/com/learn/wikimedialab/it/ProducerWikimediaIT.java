package com.learn.wikimedialab.it;

import com.learn.wikimedialab.domain.services.StreamingProcessingService;
import com.learn.wikimedialab.it.config.StreamingMockConfiguration;
import com.learn.wikimedialab.it.config.TestContainersConfiguration;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("it")
@Import({
        TestContainersConfiguration.class,
        StreamingMockConfiguration.class
})
public class ProducerWikimediaIT {
    @Autowired
    private StreamingProcessingService streamingProcessingService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Test
    void givenStreaming_whenStarted_thenPublishEventsToKafka() {

        // when
        this.streamingProcessingService.startStreaming();

        // Then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {

                    try (final Consumer<String, String> consumer =
                                 this.consumerFactory.createConsumer("test-group", null)) {

                        consumer.subscribe(List.of("wikimedia.raw.events"));

                        final ConsumerRecords<String, String> records =
                                consumer.poll(Duration.ofSeconds(2));

                        assertThat(records.count()).isGreaterThanOrEqualTo(2);
                    }
                });
    }
}
