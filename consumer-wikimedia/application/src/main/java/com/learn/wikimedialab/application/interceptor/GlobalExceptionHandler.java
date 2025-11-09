package com.learn.wikimedialab.application.interceptor;

import com.learn.wikimedialab.domain.exceptions.base.BaseWikimediaException;
import com.learn.wikimedialab.domain.exceptions.base.InvalidWikimediaException;
import com.learn.wikimedialab.domain.exceptions.base.NotFoundWikimediaException;
import com.learn.wikimedialab.domain.exceptions.base.UnauthorizedWikimediaException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for Wikimedia application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles BaseWikimediaException and returns an appropriate error response.
   *
   * @param ex the BaseWikimediaException instance
   * @return ResponseEntity containing the error response map and HTTP status
   */
  @ExceptionHandler(BaseWikimediaException.class)
  public ResponseEntity<Map<String, String>> handleVisitsException(BaseWikimediaException ex) {
    return this.buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles NotFoundWikimediaException and returns an appropriate error response.
   *
   * @param ex the NotFoundWikimediaException instance
   * @return ResponseEntity containing the error response map and HTTP status
   */
  @ExceptionHandler(NotFoundWikimediaException.class)
  public ResponseEntity<Map<String, String>> handleVisitsException(NotFoundWikimediaException ex) {
    return this.buildErrorResponse(ex, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles UnauthorizedWikimediaException and returns an appropriate error response.
   *
   * @param ex the UnauthorizedWikimediaException instance
   * @return ResponseEntity containing the error response map and HTTP status
   */
  @ExceptionHandler(UnauthorizedWikimediaException.class)
  public ResponseEntity<Map<String, String>> handleVisitsException(
      UnauthorizedWikimediaException ex) {
    return this.buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles InvalidWikimediaException and returns an appropriate error response.
   *
   * @param ex the InvalidWikimediaException instance
   * @return ResponseEntity containing the error response map and HTTP status
   */
  @ExceptionHandler(InvalidWikimediaException.class)
  public ResponseEntity<Map<String, String>> handleVisitsException(
      InvalidWikimediaException ex) {
    return this.buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
  }


  /**
   * Builds an error response entity based on the provided exception and HTTP status.
   *
   * @param ex     the BaseWikimediaException instance
   * @param status the HTTP status to be set in the response
   * @return ResponseEntity containing the error response map and specified HTTP status
   */
  private ResponseEntity<Map<String, String>> buildErrorResponse(BaseWikimediaException ex,
      HttpStatus status) {
    final Map<String, String> errorResponse = this.generateErrorResponse(ex.getCode(),
        ex.getDescription());
    return ResponseEntity.status(status).body(errorResponse);
  }

  /**
   * Generates an error response map containing the provided error code and message.
   *
   * @param errorCode    the code representing the error
   * @param errorMessage the message describing the error
   * @return a map with keys "errorCode" and "errorMessage" containing the respective values
   */
  private Map<String, String> generateErrorResponse(String errorCode, String errorMessage) {
    final Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("errorCode", errorCode);
    errorResponse.put("errorMessage", errorMessage);
    return errorResponse;
  }
}
