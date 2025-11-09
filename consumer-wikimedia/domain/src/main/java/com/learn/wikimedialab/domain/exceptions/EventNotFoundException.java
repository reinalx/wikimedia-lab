package com.learn.wikimedialab.domain.exceptions;

import com.learn.wikimedialab.domain.exceptions.base.NotFoundWikimediaException;
import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;
import lombok.Getter;

/**
 * Exception thrown when a Wikimedia event is not found.
 */
@Getter
public class EventNotFoundException extends NotFoundWikimediaException {

  @Serial
  private static final long serialVersionUID = 1372454289558545967L;

  /**
   * Constructs an EventNotFoundException with a predefined error code.
   */
  public EventNotFoundException() {
    super(ErrorCode.EVENT_NOT_FOUND);
  }
}
