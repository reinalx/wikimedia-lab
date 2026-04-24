package com.learn.wikimedialab.kafka.adapters;

import com.learn.wikimedialab.domain.adapters.EventPublisherAdapter;
import com.learn.wikimedialab.kafka.mappers.JsonToAvroMapper;
import com.wikimedia.avro.WikimediaRawEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.learn.wikimedialab.domain.utils.Constants.WIKIMEDIA_RAW_KAFKA_TOPIC;

/**
 * Implementation of the KafkaEventPublisherAdapter.
 * Converts JSON String events to Avro format and publishes them to Kafka.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapterImpl implements EventPublisherAdapter {

    private final KafkaTemplate<String, WikimediaRawEvent> kafkaTemplate;
    private final JsonToAvroMapper jsonToAvroMapper;

    /**
     * Publishes an event to the Kafka topic "wikimedia.raw.events".
     * Converts the JSON String to WikimediaRawEvent Avro format before publishing.
     *
     * @param eventData the event data as JSON String
     */
    @Override
    public void publishEvent(String eventData) {
        try {
            final WikimediaRawEvent avroEvent = this.jsonToAvroMapper.convert(eventData);
            this.kafkaTemplate.send(WIKIMEDIA_RAW_KAFKA_TOPIC, avroEvent);
            log.info("Event sent to Kafka - id: {}, type: {}, wiki: {}, user: {}",
                    avroEvent.getId(), avroEvent.getType(), avroEvent.getWiki(), avroEvent.getUser());
        } catch (final Exception e) {
            log.error("Failed to convert and publish event: {}. Event snippet: {}",
                    e.getMessage(),
                    eventData.substring(0, Math.min(200, eventData.length())));
        }
    }

}
