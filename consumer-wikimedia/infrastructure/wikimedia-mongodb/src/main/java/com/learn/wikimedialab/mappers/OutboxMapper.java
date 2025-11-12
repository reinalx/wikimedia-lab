package com.learn.wikimedialab.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.mongodb.entities.OutboxEntity;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper interface for Outbox entities.
 */
@Mapper(componentModel = "spring")
public interface OutboxMapper {

  /**
   * Maps a outbox object to its persistence entity representation.
   */
  @Mapping(target = "payload", source = "payload", qualifiedByName = "objectToJsonString")
  @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
  OutboxEntity toPersistence(Outbox<?> outbox);

  /**
   * Converts an object to its JSON string representation.
   *
   * @param payload The object to be converted.
   * @return The JSON string representation of the object.
   */
  @Named("objectToJsonString")
  @SneakyThrows
  static String objectToJsonString(Object payload) {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(payload);
  }

}
