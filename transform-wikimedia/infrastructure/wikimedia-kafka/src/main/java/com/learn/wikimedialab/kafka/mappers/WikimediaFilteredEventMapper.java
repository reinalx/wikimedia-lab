package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.wikimedia.avro.WikimediaFilteredEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WikimediaFilteredEventMapper {

  WikimediaFilteredEvent toWikimediaFilteredEvent(WikimediaEvent wikimediaEvent);

}
