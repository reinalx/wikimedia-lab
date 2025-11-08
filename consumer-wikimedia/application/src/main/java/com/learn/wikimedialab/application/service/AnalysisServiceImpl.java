package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

  /**
   * Analyzes a Wikimedia event and stores the analysis result.
   *
   * @param eventAnalysis The event analysis to be processed.
   */
  @Override
  public void analyzeEvent(EventAnalysis eventAnalysis) {

  }
}
