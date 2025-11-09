package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.events.EventAnalysis;
import com.learn.wikimedialab.domain.exceptions.EventNotFoundException;
import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPort;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AnalysisService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

  private final EventAnalysisPort eventAnalysisPort;

  private final WikimediaEventsPort wikimediaEventsPort;

  /**
   * Analyzes a Wikimedia event and stores the analysis result.
   *
   * @param eventAnalysis The event analysis to be processed.
   */
  @Override
  public void analyzeEvent(EventAnalysis eventAnalysis) {
    log.info("Analyzing event: {}", eventAnalysis);
    this.validateEventAnalysis(eventAnalysis);

    this.eventAnalysisPort.publishAnalysisEvent(eventAnalysis);
    log.info("Event analysis published: {}", eventAnalysis);
  }

  /**
   * Validates the event analysis by checking if the associated Wikimedia event exists.
   *
   * @param eventAnalysis The event analysis to be validated.
   * @throws EventNotFoundException if the associated Wikimedia event does not exist.
   */
  private void validateEventAnalysis(EventAnalysis eventAnalysis) {
    if (!this.wikimediaEventsPort.existsWikimediaEventById(eventAnalysis.eventId())) {
      throw new EventNotFoundException();
    }
  }
}
