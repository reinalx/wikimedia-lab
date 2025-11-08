package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.events.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.mappers.WikimediaEventsMapper;
import com.learn.wikimedialab.repositories.WikimediaEventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Implementation of the WikimediaEventsAdapter interface.
 */
@Component
@RequiredArgsConstructor
public class WikimediaEventsAdapter implements WikimediaEventsPort {

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

  /**
   * Retrieves a list of Wikimedia events with pagination.
   *
   * @param page the page number
   * @param size the number of events per page
   * @return a list of Wikimedia events
   */
  @Override
  public List<WikimediaEvent> getWikimediaEvents(int page, int size) {
    final Pageable pageable = PageRequest.of(page, size);
    return this.wikimediaEventRepository.findAllBy(pageable)
        .stream()
        .map(this.wikimediaEventMapper::toDomain)
        .toList();

  }
}
