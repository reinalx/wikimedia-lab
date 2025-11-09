package com.learn.wikimedialab.domain.values;

import lombok.Getter;

/**
 * Enumeration of error codes and their corresponding messages.
 */
@Getter
public enum ErrorCode {
  EVENT_NOT_FOUND(1001, "Event not found"),
  USER_NOT_FOUND(1002, "User not found"),

  JWT_EXPIRED(1003, "JWT token has expired"),
  PASSWORD_MISMATCHING(1004, "Passwords do not match");

  private final int code;

  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
