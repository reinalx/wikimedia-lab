package com.learn.wikimedialab.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikimedia.avro.WikimediaRawEvent;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka Producer settings.
 */
@Configuration
@Profile("!it")
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.schema.registry.url:http://localhost:8085}")
    private String schemaRegistryUrl;

    /**
     * Creates a ProducerFactory with Avro serialization for WikimediaRawEvent.
     *
     * @return a ProducerFactory instance
     */
    @Bean
    public ProducerFactory<String, WikimediaRawEvent> producerFactory() {
        final Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        config.put("schema.registry.url", this.schemaRegistryUrl);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Creates a KafkaTemplate for sending WikimediaRawEvent messages to Kafka topics.
     *
     * @return a KafkaTemplate instance
     */
    @Bean
    public KafkaTemplate<String, WikimediaRawEvent> kafkaTemplate() {
        return new KafkaTemplate<>(this.producerFactory());
    }

    /**
     * Creates an ObjectMapper bean for JSON parsing.
     *
     * @return an ObjectMapper instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
