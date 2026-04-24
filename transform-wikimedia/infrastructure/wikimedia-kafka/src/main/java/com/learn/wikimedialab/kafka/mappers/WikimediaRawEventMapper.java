package com.learn.wikimedialab.kafka.mappers;

import com.learn.wikimedialab.domain.events.WikimediaEvent;
import com.wikimedia.avro.WikimediaRawEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WikimediaRawEventMapper {

  WikimediaEvent toWikimediaEvent(WikimediaRawEvent wikimediaRawEvent);

}
