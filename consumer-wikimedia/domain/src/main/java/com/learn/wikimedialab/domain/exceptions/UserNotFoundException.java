package com.learn.wikimedialab.domain.exceptions;

import com.learn.wikimedialab.domain.exceptions.base.NotFoundWikimediaException;
import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends NotFoundWikimediaException {

  @Serial
  private static final long serialVersionUID = 4964352073007922752L;

  /**
   * Constructs a new UserNotFoundException with the USER_NOT_FOUND error code.
   */
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}
