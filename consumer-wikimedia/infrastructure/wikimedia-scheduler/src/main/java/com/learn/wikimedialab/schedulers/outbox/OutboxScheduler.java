package com.learn.wikimedialab.schedulers.outbox;

import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler for processing outbox events.
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxScheduler {

  private static final Logger log = LoggerFactory.getLogger(OutboxScheduler.class);

  private AnalysisService analysisService;

  /**
   * Processes pending outbox events every 10 minutes.
   */
  @Scheduled(cron = "0 */10 * * * *")
  public void processPendingEvents() {
    log.info("Starting to process pending outbox events.");

    log.info("Finished processing pending outbox events.");

  }
}
