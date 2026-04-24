package com.learn.wikimedialab.domain.ports.out.idempotence;

public interface InboxPort {

  void insertProcessedEvent(String eventId);

}
