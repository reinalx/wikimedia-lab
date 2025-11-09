package com.learn.wikimedialab.apirest.controller;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.AnalysisApi;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.model.CreateEventAnalysisRequestDTO;
import com.learn.wikimedialab.apirest.mapper.RestAnalysisMapper;
import com.learn.wikimedialab.domain.entities.auth.UserDetails;
import com.learn.wikimedialab.domain.ports.in.services.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling Wikimedia analysis API requests.
 */
@RestController
@RequiredArgsConstructor
public class WikimediaAnalysisApiController implements AnalysisApi {

  private final AnalysisService analysisService;

  private final RestAnalysisMapper restAnalysisMapper;

  /**
   * Creates an event analysis.
   *
   * @param requestDto the create event analysis request DTO
   * @return a ResponseEntity indicating the result of the operation
   */
  @Override
  public ResponseEntity<Void> createEventAnalysis(
      CreateEventAnalysisRequestDTO requestDto) {
    final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    this.analysisService.analyzeEvent(
        this.restAnalysisMapper.toEventAnalysis(requestDto).toBuilder()
            .userId(userDetails.getUserId())
            .build()
    );
    return ResponseEntity.ok().build();
  }
}
