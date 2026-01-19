package com.learn.wikimedialab.kafka.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.events.WikimediaEvent;
import java.io.IOException;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class JsonToObjectMapperTest {

  @InjectMocks
  private JsonToObjectMapper jsonToObjectMapper;

  @Mock
  private ObjectMapper objectMapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenJsonString_whenConvertJsonStringToEvent_thenReturnEvent(
      WikimediaEvent event,
      String jsonString) throws IOException {
    // Given
    when(this.objectMapper.readValue(jsonString, WikimediaEvent.class)).thenReturn(event);

    // When
    final WikimediaEvent result = this.jsonToObjectMapper.convertJsonStringToEvent(jsonString);

    // Then
    assertThat(result).isEqualTo(event);
  }


  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenConvertEventToJsonString_thenReturnJsonString(
      WikimediaEvent event,
      String jsonString) throws JsonProcessingException {
    // Given
    when(this.objectMapper.writeValueAsString(event)).thenReturn(jsonString);

    // When
    final String result = this.jsonToObjectMapper.convertEventToJsonString(event);

    // Then
    assertThat(result).isEqualTo(jsonString);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenConvertEventToJsonStringThrowsIOException_thenThrowRuntimeException(
      WikimediaEvent event) throws JsonProcessingException {
    // Given
    when(this.objectMapper.writeValueAsString(event))
        .thenThrow(new JsonProcessingException("error") {
        });

    // Then
    assertThatThrownBy(() -> this.jsonToObjectMapper.convertEventToJsonString(event))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error serializing WikimediaEvent to JSON");
  }
}
