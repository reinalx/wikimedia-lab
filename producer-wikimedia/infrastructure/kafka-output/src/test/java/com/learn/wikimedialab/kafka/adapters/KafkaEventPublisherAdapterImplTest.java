package com.learn.wikimedialab.kafka.adapters;

import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaEventPublisherAdapterImplTest {

    @InjectMocks
    private KafkaEventPublisherAdapterImpl kafkaEventPublisher;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @ParameterizedTest
    @InstancioSource(samples = 1)
    void givenEvent_whenPublishEvent_thenSendEvent(String event) {
        // When
        this.kafkaEventPublisher.publishEvent(event);

        // Then
        verify(this.kafkaTemplate, times(1)).send("wikimedia.raw.events", event);
    }
}
