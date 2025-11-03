package com.learn.wikimedialab.domain.exceptions;

import com.learn.wikimedialab.domain.exceptions.base.BaseTranformException;
import com.learn.wikimedialab.domain.values.ErrorCode;
import java.io.Serial;

/**
 * Exception thrown during processing errors.
 */
public class ProcessException extends BaseTranformException {

  @Serial
  private static final long serialVersionUID = 398223200212244925L;

  /**
   * Constructs a new ProcessException with a predefined error code.
   */
  public ProcessException() {
    super(ErrorCode.PROCESSING_ERROR);
  }
}
