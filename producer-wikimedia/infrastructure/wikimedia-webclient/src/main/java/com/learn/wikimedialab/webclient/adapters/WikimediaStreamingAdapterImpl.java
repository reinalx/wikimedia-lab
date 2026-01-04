package com.learn.wikimedialab.webclient.adapters;

import com.learn.wikimedialab.domain.adapters.StreamingClientAdapter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Implementation of the StreamingClientAdapter that streams events from Wikimedia using WebClient.
 */
@Component
public class WikimediaStreamingAdapterImpl implements StreamingClientAdapter {

  private final WebClient webClient;

  public WikimediaStreamingAdapterImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  /**
   * Streams events from the Wikimedia event source.
   *
   * @return a Flux of event data as Strings
   */
  @Override
  public Flux<String> streamEvents() {
    return this.webClient.get()
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
        })
        .filter(event -> event != null && event.data() != null)
        .map(ServerSentEvent::data);
  }
}
