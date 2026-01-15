package com.learn.wikimedialab.application.service;

import static org.instancio.Select.field;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import com.learn.wikimedialab.domain.events.MetaInfo;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WikimediaProcessorServiceImplTest {

  @InjectMocks
  private WikimediaProcessorServiceImpl wikimediaProcessorServiceImpl;

  @Mock
  private EventPublisherAdapter eventPublisherAdapter;

  @Test
  void givenEvent_whenProcessEvent_thenPublishEvent() {
    // Given
    final WikimediaEvent event = Instancio.of(WikimediaEvent.class)
        .set(field(WikimediaEvent::type), "edit")
        .set(field(WikimediaEvent::bot), false)
        .set(field(WikimediaEvent::meta), Instancio.of(MetaInfo.class)
            .set(field(MetaInfo::domain), "es.wikipedia.org")
            .create())
        .create();

    // When
    this.wikimediaProcessorServiceImpl.processEvent(event);

    // Then
    verify(this.eventPublisherAdapter).publish(event);
  }

  @Test
  void givenEventNotEdited_whenProcessEvent_thenNotPublish() {
    // Given
    final WikimediaEvent event = Instancio.of(WikimediaEvent.class)
        .set(field(WikimediaEvent::type), "delete")
        .create();
    // When
    this.wikimediaProcessorServiceImpl.processEvent(event);

    // Then
    verify(this.eventPublisherAdapter, never()).publish(event);
  }

  @Test
  void givenBotEvent_whenProcessEvent_thenNotPublish() {
    // Given
    final WikimediaEvent event = Instancio.of(WikimediaEvent.class)
        .set(field(WikimediaEvent::type), "edit")
        .set(field(WikimediaEvent::bot), true)
        .create();

    // When
    this.wikimediaProcessorServiceImpl.processEvent(event);

    // Then
    verify(this.eventPublisherAdapter, never()).publish(event);
  }

  @Test
  void givenNonSpanishEvent_whenProcessEvent_thenNotPublish() {
    // Given
    final WikimediaEvent event = Instancio.of(WikimediaEvent.class)
        .set(field(WikimediaEvent::type), "edit")
        .set(field(WikimediaEvent::bot), false)
        .set(field(WikimediaEvent::meta), Instancio.of(MetaInfo.class)
            .set(field(MetaInfo::domain), "en.wikipedia.org")
            .create())
        .create();

    // When
    this.wikimediaProcessorServiceImpl.processEvent(event);

    // Then
    verify(this.eventPublisherAdapter, never()).publish(event);
  }

}