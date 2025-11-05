package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.adapters.WikimediaEventsAdapter;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.mappers.WikimediaEventsMapper;
import com.learn.wikimedialab.repositories.WikimediaEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of the WikimediaEventsAdapter interface.
 */
@Component
@RequiredArgsConstructor
public class WikimediaEventsAdapterImpl implements WikimediaEventsAdapter {

  private final WikimediaEventRepository wikimediaEventRepository;

  private final WikimediaEventsMapper wikimediaEventMapper;

  /**
   * Saves a filtered Wikimedia event to the database.
   *
   * @param event the Wikimedia event
   */
  @Override
  public void saveFilteredEvent(WikimediaEvent event) {
    this.wikimediaEventRepository.save(
        this.wikimediaEventMapper.toEntity(event)
    );
  }
}
