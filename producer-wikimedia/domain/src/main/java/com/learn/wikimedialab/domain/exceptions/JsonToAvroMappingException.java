package com.learn.wikimedialab.domain.exceptions;

import java.io.Serial;

/**
 * Custom exception for JSON to Avro mapping errors.
 */
public class JsonToAvroMappingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 9115677844312912532L;

    public JsonToAvroMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}