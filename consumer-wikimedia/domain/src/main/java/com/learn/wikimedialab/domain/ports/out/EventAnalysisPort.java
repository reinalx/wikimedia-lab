package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.events.EventAnalysis;

/**
 * Port interface for analysis events operations.
 */
public interface EventAnalysisPort {

  /**
   * Publishes an analysis event.
   *
   * @param eventAnalysis The event analysis to be published.
   */
  void publishAnalysisEvent(EventAnalysis eventAnalysis);

}
