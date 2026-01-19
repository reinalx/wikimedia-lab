package com.learn.wikimedialab.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.entities.EventAnalysis;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class PayloadSerializerTest {

  @InjectMocks
  private PayloadSerializer payloadSerializer;

  @Mock
  private ObjectMapper objectMapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenPayload_whenToEventAnalysis_thenReturnEventAnalysis(
      Object payload, EventAnalysis eventAnalysis) {
    // Given
    when(this.objectMapper.convertValue(payload, EventAnalysis.class)).thenReturn(eventAnalysis);

    // When
    final var result = this.payloadSerializer.toEventAnalysis(payload);

    // Then
    assertThat(result).isEqualTo(eventAnalysis);
  }
}
