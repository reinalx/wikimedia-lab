package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.events.EventAnalysis;
import com.learn.wikimedialab.domain.ports.out.EventAnalysisPort;
import com.learn.wikimedialab.mappers.EventAnalysisMapper;
import com.learn.wikimedialab.repositories.EventAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter implementation for EventAnalysisPort.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventAnalysisAdapter implements EventAnalysisPort {

  private final EventAnalysisRepository eventAnalysisRepository;

  private final EventAnalysisMapper eventAnalysisMapper;

  /**
   * Publishes an analysis event by saving it to the repository.
   *
   * @param eventAnalysis The event analysis to be published.
   */
  @Override
  public void publishAnalysisEvent(EventAnalysis eventAnalysis) {
    this.eventAnalysisRepository.save(
        this.eventAnalysisMapper.toEntity(eventAnalysis)
    );
  }
}
