package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.EventAnalysis;

/**
 * Outbound port interface for publishing analysis events.
 */
public interface EventAnalysisPublisherPort {

  /**
   * Publishes an analysis event.
   *
   * @param event The analysis event to be published.
   */
  void publishAnalysisEvent(EventAnalysis event);

}
