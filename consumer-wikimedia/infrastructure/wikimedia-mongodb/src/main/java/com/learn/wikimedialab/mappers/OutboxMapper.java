package com.learn.wikimedialab.mappers;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.mongodb.entities.OutboxEntity;
import com.learn.wikimedialab.utils.PayloadSerializer;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for Outbox entities.
 */
@Mapper(componentModel = "spring", uses = PayloadSerializer.class)
public interface OutboxMapper {

  /**
   * Maps a outbox object to its persistence entity representation.
   */
  @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
  OutboxEntity toPersistence(Outbox<?> outbox);


  /**
   * Maps a list of OutboxEntity objects to a list of generic Outbox domain objects.
   */
  default List<Outbox<?>> toDomainGenericList(List<OutboxEntity> entities) {
    return entities.stream()
        .map(outboxEntity -> {
          if (EventAnalysis.class.getSimpleName().equals(outboxEntity.getAggregateType())) {
            return this.toDomain(outboxEntity);
          }
          return this.toDomainGeneric(outboxEntity);
        })
        .toList();
  }

  /**
   * Maps a OutboxEntity object to its domain representation.
   */
  @Mapping(target = "payload", source = "payload", qualifiedByName = "deserializePayloadToEventAnalysis")
  Outbox<EventAnalysis> toDomain(OutboxEntity entity);

  /**
   * Maps a OutboxEntity object to its generic domain representation.
   */
  Outbox<?> toDomainGeneric(OutboxEntity entity);

}
