package com.learn.wikimedialab.domain.exceptions.base;

import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;
import lombok.Getter;

/**
 * Base exception class for template-related errors.
 */
@Getter
public class BaseWikimediaException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -1602454289558545967L;

  private final String code;

  private final String description;

  public BaseWikimediaException(ErrorCode errorCode) {
    this.code = String.valueOf(errorCode.getCode());
    this.description = errorCode.getMessage();
  }
}
