package com.learn.wikimedialab.domain.ports.out;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;

import java.util.List;

public interface WikimediaEventsPort {

  void saveFilteredEvent(WikimediaEvent event);

  List<WikimediaEvent> getWikimediaEvents(int page, int size);
  
  boolean existsWikimediaEventById(String id);

}
