package com.learn.wikimedialab.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.mappers.WikimediaEventsMapperImpl;
import com.learn.wikimedialab.mongodb.entities.WikimediaEventEntity;
import com.learn.wikimedialab.repositories.WikimediaEventRepository;
import java.util.List;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class WikimediaEventsAdapterTest {

  @InjectMocks
  private WikimediaEventsAdapter wikimediaEventsAdapter;

  @Mock
  private WikimediaEventRepository wikimediaEventRepository;

  @Mock
  private WikimediaEventsMapperImpl wikimediaEventsMapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenSaveFilteredEvent_thenSaveEvent(WikimediaEvent wikimediaEvent,
      WikimediaEventEntity wikimediaEventEntity) {
    // When
    when(this.wikimediaEventsMapper.toEntity(wikimediaEvent))
        .thenReturn(wikimediaEventEntity);

    this.wikimediaEventsAdapter.saveFilteredEvent(wikimediaEvent);
    // Then
    verify(this.wikimediaEventRepository).save(wikimediaEventEntity);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenPageAndSize_whenGetWikimediaEvents_thenReturnWikimediaList(int page, int size,
      WikimediaEvent wikimediaEvent, WikimediaEventEntity wikimediaEventEntity) {
    // When
    when(this.wikimediaEventRepository.findAllBy(any(Pageable.class)))
        .thenReturn(List.of(wikimediaEventEntity));
    when(this.wikimediaEventsMapper.toDomain(wikimediaEventEntity))
        .thenReturn(wikimediaEvent);

    final List<WikimediaEvent> result = this.wikimediaEventsAdapter.getWikimediaEvents(page, size);

    // Then
    assertThat(result).containsExactly(wikimediaEvent);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenId_whenExistsWikimediaEventById_thenReturnTrue(String id) {
    // When
    when(this.wikimediaEventRepository.existsById(id))
        .thenReturn(true);

    final boolean result = this.wikimediaEventsAdapter.existsWikimediaEventById(id);
    // Then
    assertThat(result).isTrue();
  }
}