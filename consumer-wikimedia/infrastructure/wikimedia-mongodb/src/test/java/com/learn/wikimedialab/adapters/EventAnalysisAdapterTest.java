package com.learn.wikimedialab.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.mappers.EventAnalysisMapperImpl;
import com.learn.wikimedialab.mongodb.entities.EventAnalysisEntity;
import com.learn.wikimedialab.repositories.EventAnalysisRepository;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class EventAnalysisAdapterTest {

  @InjectMocks
  private EventAnalysisAdapter eventAnalysisAdapter;

  @Mock
  private EventAnalysisRepository eventAnalysisRepository;

  @Mock
  private EventAnalysisMapperImpl eventAnalysisMapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEventAnalysis_whenPublisAnalysisEvent_thenReturnEventAnalysisSaved(
      EventAnalysis eventAnalysis, EventAnalysisEntity eventAnalysisEntity) {
    // When
    when(this.eventAnalysisMapper.toEntity(eventAnalysis)).thenReturn(eventAnalysisEntity);
    when(this.eventAnalysisRepository.save(eventAnalysisEntity)).thenReturn(eventAnalysisEntity);

    final var result = this.eventAnalysisAdapter.publishAnalysisEvent(eventAnalysis);

    // Then
    assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(eventAnalysis);
    assertThat(result.id()).isEqualTo(eventAnalysisEntity.getId());
  }


}