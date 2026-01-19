package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import com.learn.wikimedialab.domain.adapters.StreamingClientAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamingProcessingServiceImplTest {

    @InjectMocks
    private StreamingProcessingServiceImpl streamingProcessingService;

    @Mock
    private StreamingClientAdapter streamingClientAdapter;

    @Mock
    private EventPublisherAdapter eventPublisherAdapter;

    @Captor
    private ArgumentCaptor<String> eventCaptor;

    @Test
    void startStreaming_shouldPublishValidJsonEvents() {
        // Given
        final String validEvent1 = "{\"id\":\"1\",\"type\":\"edit\"}";
        final String validEvent2 = "{\"id\":\"2\",\"type\":\"new\"}";
        final Flux<String> eventFlux = Flux.just(validEvent1, validEvent2);

        when(this.streamingClientAdapter.streamEvents()).thenReturn(eventFlux);
        doNothing().when(this.eventPublisherAdapter).publishEvent(anyString());

        // When
        this.streamingProcessingService.startStreaming();

        // Then
        verify(this.eventPublisherAdapter, timeout(1000).times(2)).publishEvent(this.eventCaptor.capture());

        assertThat(this.eventCaptor.getAllValues())
                .hasSize(2)
                .containsExactly(validEvent1, validEvent2);
    }

    @Test
    void startStreaming_shouldFilterOutNonJsonEvents() {
        // Given
        final String validEvent = "{\"id\":\"1\"}";
        final String invalidEvent = "data: some non-json text";
        final Flux<String> eventFlux = Flux.just(validEvent, invalidEvent);

        when(this.streamingClientAdapter.streamEvents()).thenReturn(eventFlux);
        doNothing().when(this.eventPublisherAdapter).publishEvent(anyString());

        // When
        this.streamingProcessingService.startStreaming();

        // Then
        verify(this.eventPublisherAdapter, timeout(1000).times(1)).publishEvent(validEvent);
        verify(this.eventPublisherAdapter, never()).publishEvent(invalidEvent);
    }


    @Test
    void startStreaming_shouldHandleEmptyStream() {
        // Given
        final Flux<String> emptyFlux = Flux.empty();

        when(this.streamingClientAdapter.streamEvents()).thenReturn(emptyFlux);

        // When
        this.streamingProcessingService.startStreaming();

        // Then
        verify(this.eventPublisherAdapter, after(500).never()).publishEvent(anyString());
    }

    @Test
    void startStreaming_shouldCallStreamingClientAdapter() {
        // Given
        final Flux<String> eventFlux = Flux.just("{\"id\":\"1\"}");
        when(this.streamingClientAdapter.streamEvents()).thenReturn(eventFlux);
        doNothing().when(this.eventPublisherAdapter).publishEvent(anyString());

        // When
        this.streamingProcessingService.startStreaming();

        // Then
        verify(this.streamingClientAdapter, times(1)).streamEvents();
    }
}
