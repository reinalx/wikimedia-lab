package com.learn.wikimedialab.domain.entities.auth;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * Class representing user details for authentication purposes.
 */
@Data
@Builder(toBuilder = true)
public class UserDetails implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String userId;

  private String username;

  private String role;

}
