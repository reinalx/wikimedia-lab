package com.learn.wikimedialab.domain.exceptions;

import com.learn.wikimedialab.domain.exceptions.base.InvalidWikimediaException;
import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;

/**
 * Exception thrown when passwords do not match.
 */
public class PasswordMismatchingException extends InvalidWikimediaException {

  @Serial
  private static final long serialVersionUID = 4951273599663331227L;

  /**
   * Constructs a new PasswordMismatchingException with a predefined error code.
   */
  public PasswordMismatchingException() {
    super(ErrorCode.PASSWORD_MISMATCHING);
  }
}
