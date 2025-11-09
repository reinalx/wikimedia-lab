package com.learn.wikimedialab.domain.exceptions;

import com.learn.wikimedialab.domain.exceptions.base.UnauthorizedWikimediaException;
import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;

/**
 * Exception thrown when JWT token has expired.
 */
public class JwtExpiredException extends UnauthorizedWikimediaException {


  @Serial
  private static final long serialVersionUID = -6247970265613401470L;

  /**
   * Constructs a new JwtExpiredException with a predefined error code.
   */
  public JwtExpiredException() {
    super(ErrorCode.JWT_EXPIRED);
  }
}
