package com.learn.wikimedialab.application.service;

import com.learn.wikimedialab.domain.services.WikimediaProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the WikimediaProcessorService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WikimediaProcessorServiceImpl implements WikimediaProcessorService {

  /**
   * Processes a raw Wikimedia event.
   *
   * @param rawEvent the raw event data as a String
   */
  @Override
  public void processEvent(String rawEvent) {

  }
}
