package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import java.util.List;

/**
 * Outbound port interface for publishing analysis events.
 */
public interface EventAnalysisPublisherPort {

  /**
   * Publishes analysis events to an external system.
   *
   * @param events The analysis events to be published.
   */
  void publishAnalysisEvent(List<EventAnalysis> events);

}
