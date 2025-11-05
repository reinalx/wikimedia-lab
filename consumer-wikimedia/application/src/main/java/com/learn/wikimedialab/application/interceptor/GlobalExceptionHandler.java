package com.learn.wikimedialab.application.interceptor;

import com.learn.wikimedialab.domain.exceptions.base.BaseTemplateException;
import com.learn.wikimedialab.domain.exceptions.base.NotFoundTemplateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseTemplateException.class)
    public ResponseEntity<Map<String, String>> handleVisitsException(BaseTemplateException ex) {
        return this.buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundTemplateException.class)
    public ResponseEntity<Map<String, String>> handleVisitsException(NotFoundTemplateException ex) {
        return this.buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Map<String, String>> buildErrorResponse(BaseTemplateException ex, HttpStatus status) {
        final Map<String, String> errorResponse = this.generateErrorResponse(ex.getCode(), ex.getDescription());
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Generates an error response map containing the provided error code and message.
     *
     * @param errorCode the code representing the error
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
