package com.learn.wikimedialab.mappers;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.learn.wikimedialab.mongodb.entities.WikimediaEventEntity;
import org.mapstruct.Mapper;

/**
 * Mapper interface for Wikimedia events.
 */
@Mapper(componentModel = "spring")
public interface WikimediaEventsMapper {

  /**
   * Maps a WikimediaEvent to a WikimediaEventEntity.
   *
   * @param event the WikimediaEvent to map
   * @return the mapped WikimediaEventEntity
   */
  WikimediaEventEntity toEntity(WikimediaEvent event);

  /**
   * Maps a WikimediaEventEntity to a WikimediaEvent.
   *
   * @param entity the WikimediaEventEntity to map
   * @return the mapped WikimediaEvent
   */
  WikimediaEvent toDomain(WikimediaEventEntity entity);
}
