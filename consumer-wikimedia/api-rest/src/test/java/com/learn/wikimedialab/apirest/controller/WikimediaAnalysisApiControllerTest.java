package com.learn.wikimedialab.apirest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.model.CreateEventAnalysisRequestDTO;
import com.learn.wikimedialab.apirest.mapper.RestAnalysisMapper;
import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.auth.UserDetails;
import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WikimediaAnalysisApiControllerTest {

  @InjectMocks
  private WikimediaAnalysisApiController wikimediaAnalysisApiController;

  @Mock
  private AnalysisService analysisService;

  @Mock
  private RestAnalysisMapper restAnalysisMapper;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private Authentication authentication;

  private UserDetails userDetails;

  @BeforeEach
  void setUp() {
    this.userDetails = Instancio.create(UserDetails.class);

    when(this.securityContext.getAuthentication()).thenReturn(this.authentication);
    when(this.authentication.getPrincipal()).thenReturn(this.userDetails);

    SecurityContextHolder.setContext(this.securityContext);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenAnalysis_whenCreateEventAnalysis_thenSuccess(
      CreateEventAnalysisRequestDTO createEventAnalysisRequestDTO, EventAnalysis eventAnalysis) {
    // Given
    final ArgumentCaptor<EventAnalysis> captor = ArgumentCaptor.forClass(EventAnalysis.class);
    // When
    when(this.restAnalysisMapper.toEventAnalysis(createEventAnalysisRequestDTO))
        .thenReturn(eventAnalysis);

    final var result = this.wikimediaAnalysisApiController.createEventAnalysis(
        createEventAnalysisRequestDTO);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();

    verify(this.analysisService).analyzeEvent(captor.capture());
    final var capturedEventAnalysis = captor.getValue();
    assertThat(capturedEventAnalysis).isNotNull().extracting(EventAnalysis::userId)
        .isEqualTo(this.userDetails.getUserId());
  }

}