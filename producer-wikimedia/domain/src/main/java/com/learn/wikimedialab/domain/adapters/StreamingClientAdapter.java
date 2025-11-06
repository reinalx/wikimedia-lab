package com.learn.wikimedialab.domain.adapters;

import reactor.core.publisher.Flux;

/**
 * Port interface for streaming client operations.
 */
public interface StreamingClientAdapter {

  /**
   * Streams events as a Flux of Strings.
   *
   * @return a Flux stream of event strings
   */
  Flux<String> streamEvents();

}
