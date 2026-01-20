package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.wikimedia.avro.WikimediaFilteredEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WikimediaFilteredEventMapper {

  WikimediaEvent toWikimediaEvent(WikimediaFilteredEvent wikimediaFilteredEvent);

}
