package com.learn.wikimedialab.mongodb.entities.idempotence;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inbox")
public class InboxEntity {

  @Id
  private String id;
  
  private Instant processedAt = Instant.now();

}
