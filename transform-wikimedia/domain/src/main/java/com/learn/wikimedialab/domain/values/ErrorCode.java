package com.learn.wikimedialab.domain.values;

import lombok.Getter;

/**
 * Enumeration of error codes and their corresponding messages.
 */
@Getter
public enum ErrorCode {
  PROCESSING_ERROR(1001, "Error occurred during processing Wikimedia event");

  private final int code;

  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
