package com.learn.wikimedialab.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing a User stored in MongoDB.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {

  @Id
  private String id;

  @Indexed(unique = true)
  private String username;

  private String password;

  private String role;

  @Builder.Default
  private Long createdAt = System.currentTimeMillis();
}