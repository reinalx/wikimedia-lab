package com.learn.wikimedialab.application.interceptor;

import com.learn.wikimedialab.domain.exceptions.base.BaseTemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleBaseVisitsException_ShouldReturnInternalServerErrorResponse() {
        // Given
        final BaseTemplateException mockException = mock(BaseTemplateException.class);
        final String expectedErrorCode = "ERROR_CODE";
        final String expectedErrorMessage = "Error description";

        when(mockException.getCode()).thenReturn(expectedErrorCode);
        when(mockException.getDescription()).thenReturn(expectedErrorMessage);

        // When
        final ResponseEntity<Map<String, String>> response = this.globalExceptionHandler.handleVisitsException(mockException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedErrorCode, Objects.requireNonNull(response.getBody()).get("errorCode"));
        assertEquals(expectedErrorMessage, response.getBody().get("errorMessage"));
    }
}