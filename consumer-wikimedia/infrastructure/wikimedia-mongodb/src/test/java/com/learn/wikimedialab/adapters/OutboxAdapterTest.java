package com.learn.wikimedialab.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learn.wikimedialab.domain.entities.EventAnalysis;
import com.learn.wikimedialab.domain.entities.outbox.Outbox;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import com.learn.wikimedialab.mappers.OutboxMapper;
import com.learn.wikimedialab.mongodb.entities.OutboxEntity;
import com.learn.wikimedialab.repositories.OutboxRepository;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class OutboxAdapterTest {

  @InjectMocks
  private OutboxAdapter outboxAdapter;

  @Mock
  private OutboxRepository outboxRepository;

  @Mock
  private OutboxMapper outboxMapper;

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenEvent_whenSaveEvent_thenSuccess(Outbox<EventAnalysis> outbox,
      OutboxEntity outboxEntity) {
    // When
    when(this.outboxMapper.toPersistence(outbox)).thenReturn(outboxEntity);
    when(this.outboxRepository.save(outboxEntity)).thenReturn(outboxEntity);

    this.outboxAdapter.saveEvent(outbox);

    // Then
    verify(this.outboxRepository, times(1)).save(outboxEntity);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void whenFetchPendingEvents_thenReturnPendingEvents(List<Outbox<EventAnalysis>> outboxes,
      List<OutboxEntity> outboxEntities) {
    // When
    when(this.outboxRepository.findByStatus(OutboxStatus.PENDING)).thenReturn(outboxEntities);
    when(this.outboxMapper.toDomainList(outboxEntities)).thenReturn(outboxes);

    final var result = this.outboxAdapter.fetchPendingEvents();

    // Then
    assertThat(result).isEqualTo(outboxes);
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenOutboxes_whenUpdateOutboxStatusToProcessed_thenUpdateStatus(
      List<Outbox<EventAnalysis>> outboxes) {
    // Given
    final List<String> outboxIds = outboxes.stream()
        .map(Outbox::id)
        .toList();
    final OutboxEntity outboxEntity = Instancio.create(OutboxEntity.class).toBuilder()
        .status(OutboxStatus.PENDING)
        .build();
    final OutboxEntity outboxEntity1 = Instancio.create(OutboxEntity.class).toBuilder()
        .status(OutboxStatus.PENDING)
        .build();
    final List<OutboxEntity> outboxEntities = List.of(outboxEntity, outboxEntity1);
    final ArgumentCaptor<List<OutboxEntity>> captor = ArgumentCaptor.forClass(List.class);

    // When
    when(this.outboxRepository.findAllById(outboxIds)).thenReturn(outboxEntities);

    this.outboxAdapter.updateOutboxStatusToProcessed(outboxes);

    // Then
    verify(this.outboxRepository).saveAll(captor.capture());
    final List<OutboxEntity> updatedOutboxes = captor.getValue();

    assertThat(updatedOutboxes).allMatch(
        outbox -> outbox.getStatus() == OutboxStatus.PROCESSED
    );


  }


}