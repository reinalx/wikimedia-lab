package com.learn.wikimedialab.schedulers.outbox;

import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler for processing outbox events.
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxScheduler {

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
