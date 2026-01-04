package com.learn.wikimedialab.webclient.adapters;

import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WikimediaStreamingAdapterImplTest {

    @InjectMocks
    private WikimediaStreamingAdapterImpl wikimediaStreamingAdapter;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    @SuppressWarnings("unchecked")
    void givenValidEvents_whenStreamEvents_thenReturnsFluxOfEventData() {
        // Given
        final String eventData1 = Instancio.create(String.class);
        final String eventData2 = Instancio.create(String.class);
        final String eventData3 = Instancio.create(String.class);

        final ServerSentEvent<String> event1 = ServerSentEvent.<String>builder().data(eventData1).build();
        final ServerSentEvent<String> event2 = ServerSentEvent.<String>builder().data(eventData2).build();
        final ServerSentEvent<String> event3 = ServerSentEvent.<String>builder().data(eventData3).build();

        final Flux<ServerSentEvent<String>> eventFlux = Flux.just(event1, event2, event3);

        when(this.webClient.get()).thenReturn(this.requestHeadersUriSpec);
        when(this.requestHeadersUriSpec.accept(any())).thenReturn(this.requestHeadersSpec);
        when(this.requestHeadersSpec.retrieve()).thenReturn(this.responseSpec);
        when(this.responseSpec.bodyToFlux(any(ParameterizedTypeReference.class))).thenReturn(eventFlux);

        // When
        final Flux<String> result = this.wikimediaStreamingAdapter.streamEvents();

        // Then
        StepVerifier.create(result)
                .expectNext(eventData1)
                .expectNext(eventData2)
                .expectNext(eventData3)
                .verifyComplete();

        verify(this.webClient).get();
        verify(this.requestHeadersUriSpec).accept(any());
        verify(this.requestHeadersSpec).retrieve();
        verify(this.responseSpec).bodyToFlux(any(ParameterizedTypeReference.class));
    }


    @Test
    @SuppressWarnings("unchecked")
    void givenEventsWithNullData_whenStreamEvents_thenFiltersOutEventsWithNullData() {
        // Given
        final String eventData1 = Instancio.create(String.class);
        final String eventData2 = Instancio.create(String.class);

        final ServerSentEvent<String> validEvent1 = ServerSentEvent.<String>builder().data(eventData1).build();
        final ServerSentEvent<String> eventWithNullData = ServerSentEvent.<String>builder().data(null).build();
        final ServerSentEvent<String> validEvent2 = ServerSentEvent.<String>builder().data(eventData2).build();

        final Flux<ServerSentEvent<String>> eventFlux = Flux.just(validEvent1, eventWithNullData, validEvent2);

        when(this.webClient.get()).thenReturn(this.requestHeadersUriSpec);
        when(this.requestHeadersUriSpec.accept(any())).thenReturn(this.requestHeadersSpec);
        when(this.requestHeadersSpec.retrieve()).thenReturn(this.responseSpec);
        when(this.responseSpec.bodyToFlux(any(ParameterizedTypeReference.class))).thenReturn(eventFlux);

        // When
        final Flux<String> result = this.wikimediaStreamingAdapter.streamEvents();

        // Then
        StepVerifier.create(result)
                .expectNext(eventData1)
                .expectNext(eventData2)
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenEmptyStream_whenStreamEvents_thenReturnsEmptyFlux() {
        // Given
        final Flux<ServerSentEvent<String>> emptyFlux = Flux.empty();

        when(this.webClient.get()).thenReturn(this.requestHeadersUriSpec);
        when(this.requestHeadersUriSpec.accept(any())).thenReturn(this.requestHeadersSpec);
        when(this.requestHeadersSpec.retrieve()).thenReturn(this.responseSpec);
        when(this.responseSpec.bodyToFlux(any(ParameterizedTypeReference.class))).thenReturn(emptyFlux);

        // When
        final Flux<String> result = this.wikimediaStreamingAdapter.streamEvents();

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenWebClientError_whenStreamEvents_thenPropagatesError() {
        // Given
        final RuntimeException expectedException = new RuntimeException("Connection error");
        final Flux<ServerSentEvent<String>> errorFlux = Flux.error(expectedException);

        when(this.webClient.get()).thenReturn(this.requestHeadersUriSpec);
        when(this.requestHeadersUriSpec.accept(any())).thenReturn(this.requestHeadersSpec);
        when(this.requestHeadersSpec.retrieve()).thenReturn(this.responseSpec);
        when(this.responseSpec.bodyToFlux(any(ParameterizedTypeReference.class))).thenReturn(errorFlux);

        // When
        final Flux<String> result = this.wikimediaStreamingAdapter.streamEvents();

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Connection error"))
                .verify();
    }

}
