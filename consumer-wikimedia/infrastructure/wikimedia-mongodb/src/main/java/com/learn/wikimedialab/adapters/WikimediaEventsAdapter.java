package com.learn.wikimedialab.adapters;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.mappers.WikimediaEventsMapper;
import com.learn.wikimedialab.repositories.WikimediaEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the WikimediaEventsAdapter interface.
 */
@Component
@RequiredArgsConstructor
public class WikimediaEventsAdapter implements WikimediaEventsPort {

  private final WikimediaEventRepository wikimediaEventRepository;

  private final WikimediaEventsMapper wikimediaEventMapper;

  @Override
  public void saveFilteredEvent(WikimediaEvent event) {
    this.wikimediaEventRepository.save(
        this.wikimediaEventMapper.toEntity(event)
    );
  }

  @Override
  public List<WikimediaEvent> getWikimediaEvents(int page, int size) {
    final Pageable pageable = PageRequest.of(page, size);
    return this.wikimediaEventRepository.findAllBy(pageable)
        .stream()
        .map(this.wikimediaEventMapper::toDomain)
        .toList();

  }

  @Override
  public boolean existsWikimediaEventById(String id) {
    return this.wikimediaEventRepository.existsById(id);
  }
}
