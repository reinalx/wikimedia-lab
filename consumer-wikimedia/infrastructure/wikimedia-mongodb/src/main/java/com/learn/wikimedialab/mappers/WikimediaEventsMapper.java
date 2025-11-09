package com.learn.wikimedialab.mappers;

import com.learn.wikimedialab.domain.entities.events.WikimediaEvent;
import com.learn.wikimedialab.mongodb.entities.WikimediaEventEntity;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
   * Converts an OffsetDateTime to a Long timestamp.
   *
   * @param dateTime the OffsetDateTime to convert
   * @return the corresponding timestamp in milliseconds
   */
  default Long toTimestamp(OffsetDateTime dateTime) {
    if (dateTime == null) {
      return Instant.now().toEpochMilli();
    }
    return dateTime.toInstant().toEpochMilli();
  }

  /**
   * Maps a WikimediaEventEntity to a WikimediaEvent.
   *
   * @param entity the WikimediaEventEntity to map
   * @return the mapped WikimediaEvent
   */
  WikimediaEvent toDomain(WikimediaEventEntity entity);

  /**
   * Converts a Long timestamp to an OffsetDateTime.
   *
   * @param createdAt the timestamp in milliseconds
   * @return the corresponding OffsetDateTime
   */
  default OffsetDateTime toOffsetDateTime(Long createdAt) {
    if (createdAt == null) {
      return OffsetDateTime.now().withNano(0);
    }
    return Instant.ofEpochMilli(createdAt)
        .atOffset(ZoneOffset.UTC)
        .withNano(0);
  }
}
