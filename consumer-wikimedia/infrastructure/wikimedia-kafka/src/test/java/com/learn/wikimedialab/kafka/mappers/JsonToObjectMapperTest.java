package com.learn.wikimedialab.kafka.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import java.io.IOException;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class JsonToObjectMapperTest {

  @InjectMocks
  private JsonToObjectMapper jsonToObjectMapper;

  @Mock
  private ObjectMapper objectMapper;

  @Test
  void givenValidJsonString_whenConvertJsonStringToEvent_thenReturnWikimediaEvent()
      throws JsonProcessingException {
    // Given
    final String jsonString = "{\"id\":\"123\",\"type\":\"edit\"}";
    final WikimediaEvent expectedEvent = Instancio.create(WikimediaEvent.class);

    when(this.objectMapper.readValue(eq(jsonString), eq(WikimediaEvent.class)))
        .thenReturn(expectedEvent);

    // When
    final WikimediaEvent result = this.jsonToObjectMapper.convertJsonStringToEvent(jsonString);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(expectedEvent);
  }

  @Test
  void givenInvalidJsonString_whenConvertJsonStringToEvent_thenThrowRuntimeException()
      throws JsonProcessingException {
    // Given
    final String invalidJsonString = "invalid json";
    final JsonProcessingException jsonException = new JsonProcessingException("Invalid JSON") {};

    when(this.objectMapper.readValue(eq(invalidJsonString), eq(WikimediaEvent.class)))
        .thenThrow(jsonException);

    // When & Then
    assertThatThrownBy(() -> this.jsonToObjectMapper.convertJsonStringToEvent(invalidJsonString))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error deserializing JSON to WikimediaEvent")
        .hasCause(jsonException);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "null",
      "{}",
      "{\"invalid\":\"data\"}"
  })
  void givenVariousInvalidInputs_whenConvertJsonStringToEvent_thenHandleGracefully(
      String jsonString) throws JsonProcessingException {
    // Given
    final JsonProcessingException jsonException = new JsonProcessingException("Deserialization error") {};

    when(this.objectMapper.readValue(eq(jsonString), eq(WikimediaEvent.class)))
        .thenThrow(jsonException);

    // When & Then
    assertThatThrownBy(() -> this.jsonToObjectMapper.convertJsonStringToEvent(jsonString))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Error deserializing JSON to WikimediaEvent");
  }

  @Test
  void givenNullJsonString_whenConvertJsonStringToEvent_thenThrowRuntimeException()
      throws JsonProcessingException {
    // Given
    final String nullJsonString = null;
    final JsonProcessingException jsonException = new JsonProcessingException("Cannot deserialize null") {};

    when(this.objectMapper.readValue(eq(nullJsonString), eq(WikimediaEvent.class)))
        .thenThrow(jsonException);

    // When & Then
    assertThatThrownBy(() -> this.jsonToObjectMapper.convertJsonStringToEvent(nullJsonString))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error deserializing JSON to WikimediaEvent")
        .hasCause(jsonException);
  }
}
