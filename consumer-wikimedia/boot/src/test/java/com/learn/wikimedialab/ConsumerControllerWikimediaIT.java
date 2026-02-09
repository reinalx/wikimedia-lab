package com.learn.wikimedialab;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.analysis.model.CreateEventAnalysisRequestDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.model.GetWikimediaEventsResponseDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserRequestDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserResponseDTO;
import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import com.learn.wikimedialab.domain.ports.out.WikimediaEventsPort;
import com.learn.wikimedialab.domain.ports.out.idempotence.OutboxPort;
import com.learn.wikimedialab.domain.values.OutboxEventType;
import com.learn.wikimedialab.domain.values.OutboxStatus;
import com.learn.wikimedialab.mongodb.entities.EventAnalysisEntity;
import com.learn.wikimedialab.mongodb.entities.idempotence.OutboxEntity;
import com.learn.wikimedialab.repositories.EventAnalysisRepository;
import org.awaitility.Awaitility;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@Import({TestcontainersConfiguration.class, InstancioExtension.class})
public class ConsumerControllerWikimediaIT {

  private static final String BASE = "/api/wikimedia-events";

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  WikimediaEventsPort wikimediaEventsPort;

  @Autowired
  OutboxPort outboxPort;

  @Autowired
  EventAnalysisRepository analysisRepository;

  @Value("${app.kafka.topics.filtered-events}")
  String filteredEventsTopic;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.objectMapper = new ObjectMapper();
    this.mongoTemplate.getDb().drop();
  }

  @Test
  void givenFilteredEventsPublished_whenCallGetEventsApi_thenReturnEvents() {

    // Given
    final WikimediaEvent event1 = this.validEvent("1");
    final WikimediaEvent event2 = this.validEvent("2");

    // When

    this.publishFilteredEvent(event1);
    this.publishFilteredEvent(event2);

    // Then
    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> {
          final ResponseEntity<GetWikimediaEventsResponseDTO> response =
              this.callGetEventsApi(this.loginAndGetToken());

          assertThat(response.getBody().getEvents()).hasSize(2);
          // COMPARAR LO RECIBIDO CON LO ENVIADo
        });
  }

  private WikimediaEvent validEvent(String id) {
    return Instancio.of(WikimediaEvent.class)
        .set(field(WikimediaEvent::id), id)
        .create();
  }

  private void publishFilteredEvent(WikimediaEvent event) {
    try {
      final String json = this.objectMapper.writeValueAsString(event);
      this.kafkaTemplate.send(this.filteredEventsTopic, json);
      this.kafkaTemplate.flush();
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private ResponseEntity<GetWikimediaEventsResponseDTO> callGetEventsApi(String token) {

    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    final HttpEntity<Void> entity = new HttpEntity<>(headers);

    return this.restTemplate.exchange(
        BASE + "/v1/events?page=0&size=10",
        HttpMethod.GET,
        entity,
        GetWikimediaEventsResponseDTO.class
    );
  }

  private String loginAndGetToken() {

    final LoginUserRequestDTO login = new LoginUserRequestDTO();
    login.setUsername("editor_user");
    login.setPassword("editor_password");

    final ResponseEntity<LoginUserResponseDTO> response =
        this.restTemplate.postForEntity(
            BASE + "/v1/auth/login",
            login,
            LoginUserResponseDTO.class
        );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    return response.getBody().getToken();
  }

  @ParameterizedTest
  @InstancioSource(samples = 1)
  void givenExistingEvent_whenCreateAnalysis_thenAnalysisAndOutboxArePersisted(String eventId) {

    // Given
    final WikimediaEvent event = this.persistEvent(eventId);
    final CreateEventAnalysisRequestDTO request = new CreateEventAnalysisRequestDTO();
    request.setEventId(event.id());
    request.setAnalysis("This is an analysis");

    final String token = this.loginAndGetToken();

    // When
    final HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    final HttpEntity<CreateEventAnalysisRequestDTO> entity =
        new HttpEntity<>(request, headers);

    final ResponseEntity<Void> response =
        this.restTemplate.postForEntity(
            BASE + "/v1/analysis",
            entity,
            Void.class
        );

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<EventAnalysisEntity> analyses = this.analysisRepository.findAll();
    assertThat(analyses).hasSize(1);
    assertThat(analyses.getFirst().getId()).isEqualTo(eventId);

    final List<OutboxEntity> outboxes =
        this.mongoTemplate.findAll(OutboxEntity.class);

    assertThat(outboxes).hasSize(1);

    final OutboxEntity outbox = outboxes.getFirst();
    assertThat(outbox).extracting(OutboxEntity::getId, OutboxEntity::getStatus,
            OutboxEntity::getEventType, OutboxEntity::getAggregateId)
        .containsExactly(analyses.getFirst().getId(), OutboxStatus.PENDING,
            OutboxEventType.CREATE.toString(), eventId);
  }


  private WikimediaEvent persistEvent(String eventId) {
    final WikimediaEvent event = Instancio.create(WikimediaEvent.class).toBuilder()
        .id(eventId)
        .build();
    this.wikimediaEventsPort.saveFilteredEvent(event);
    return event;
  }

}
