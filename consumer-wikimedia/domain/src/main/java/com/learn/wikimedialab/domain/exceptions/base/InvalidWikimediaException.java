package com.learn.wikimedialab.domain.exceptions.base;

import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;

/**
 * Exception thrown for invalid Wikimedia operations.
 */
public class InvalidWikimediaException extends BaseWikimediaException {

  @Serial
  private static final long serialVersionUID = 5247287427968806824L;

  /**
   * Constructs a new InvalidWikimediaException with the specified error code.
   *
   * @param errorCode the error code representing the specific error
   */
  public InvalidWikimediaException(ErrorCode errorCode) {
    super(errorCode);
  }
}
