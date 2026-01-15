package com.learn.wikimedialab;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.events.model.GetWikimediaEventsResponseDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserRequestDTO;
import com.learn.wikimedialab.apigenerator.openapi.api.wikimedia.users.model.LoginUserResponseDTO;
import com.learn.wikimedialab.domain.entities.WikimediaEvent;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@Import({TestcontainersConfiguration.class, InstancioExtension.class})
public class ConsumerWikimediaIT {

  private static final String BASE = "/api/wikimedia-events";

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  MongoTemplate mongoTemplate;

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


}
