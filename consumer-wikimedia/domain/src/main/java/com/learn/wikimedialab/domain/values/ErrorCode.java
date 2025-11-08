package com.learn.wikimedialab.domain.values;

import lombok.Getter;

/**
 * Enumeration of error codes and their corresponding messages.
 */
@Getter
public enum ErrorCode {
  PRODUCT_NOT_FOUND(1001, "Product not found");

  private final int code;

  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
