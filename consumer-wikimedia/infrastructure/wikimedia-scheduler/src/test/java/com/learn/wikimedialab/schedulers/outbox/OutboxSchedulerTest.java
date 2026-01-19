package com.learn.wikimedialab.schedulers.outbox;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.learn.wikimedialab.application.service.OutboxServiceImpl;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class OutboxSchedulerTest {

  @InjectMocks
  private OutboxScheduler outboxScheduler;

  @Mock
  private OutboxServiceImpl outboxService;

  @Test
  void whenProcesPendingEvents_thenCallService() {
    // When
    this.outboxScheduler.processPendingEvents();

    // Then
    verify(this.outboxService, times(1)).processPendingEvents();
  }

}