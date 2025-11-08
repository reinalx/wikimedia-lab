package com.learn.wikimedialab.apirest.controller;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.AnalysisApi;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.model.CreateEventAnalysisRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling Wikimedia analysis API requests.
 */
@RestController
@RequiredArgsConstructor
public class WikimediaAnalysisApiController implements AnalysisApi {

  @Override
  public ResponseEntity<Void> createEventAnalysis(
      CreateEventAnalysisRequestDTO createEventAnalysisRequestDTO) {
    return null;
  }
}
