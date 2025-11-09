package com.learn.wikimedialab.domain.exceptions.base;

import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;

/**
 * Exception class for unauthorized access errors in Wikimedia domain.
 */
public class UnauthorizedWikimediaException extends BaseWikimediaException {

  @Serial
  private static final long serialVersionUID = 1569301900745692265L;

  /**
   * Constructs an UnauthorizedWikimediaException with the specified error code.
   *
   * @param errorCode the error code representing the unauthorized access error
   */
  public UnauthorizedWikimediaException(ErrorCode errorCode) {
    super(errorCode);
  }
}
