package com.learn.wikimedialab.domain.exceptions.base;

import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;
import lombok.Getter;

/**
 * Exception class for not found errors in Wikimedia domain.
 */
@Getter
public class NotFoundWikimediaException extends BaseWikimediaException {

  @Serial
  private static final long serialVersionUID = -1602454289558545967L;

  /**
   * Constructs a NotFoundWikimediaException with the specified error code.
   *
   * @param errorCode the error code representing the not found error
   */
  public NotFoundWikimediaException(ErrorCode errorCode) {
    super(errorCode);
  }
}
