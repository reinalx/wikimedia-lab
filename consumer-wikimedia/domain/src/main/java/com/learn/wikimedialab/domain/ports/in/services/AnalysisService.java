package com.learn.wikimedialab.domain.ports.in.services;

import com.learn.wikimedialab.domain.entities.EventAnalysis;

/**
 * Service interface for analyzing Wikimedia events.
 */
public interface AnalysisService {

  /**
   * Analyzes a Wikimedia event and stores the analysis result.
   *
   * @param eventAnalysis The event analysis to be processed.
   */
  void analyzeEvent(EventAnalysis eventAnalysis);
}
