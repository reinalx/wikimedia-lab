package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
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

  /**
   * Checks if a Wikimedia event exists by its ID.
   *
   * @param id the ID of the Wikimedia event
   * @return true if the event exists, false otherwise
   */
  @Override
  public boolean existsWikimediaEventById(String id) {
    return this.wikimediaEventRepository.existsById(id);
  }
}
