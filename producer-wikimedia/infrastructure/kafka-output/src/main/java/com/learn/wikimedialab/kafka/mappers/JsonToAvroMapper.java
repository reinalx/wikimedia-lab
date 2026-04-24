package com.learn.wikimedialab.kafka.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.wikimedialab.domain.exceptions.JsonToAvroMappingException;
import com.wikimedia.avro.Meta;
import com.wikimedia.avro.WikimediaRawEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Mapper to convert JSON String from Wikimedia stream to WikimediaRawEvent Avro object.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonToAvroMapper {

    private final ObjectMapper objectMapper;

    /**
     * Converts a JSON String to a WikimediaRawEvent Avro object.
     *
     * @param jsonString the JSON string from Wikimedia stream
     * @return WikimediaRawEvent Avro object
     * @throws JsonToAvroMappingException if the JSON cannot be parsed or mapped
     */
    public WikimediaRawEvent convert(String jsonString) {
        try {
            final JsonNode root = this.objectMapper.readTree(jsonString);

            final Meta meta = this.buildMeta(root.path("meta"));

            return WikimediaRawEvent.newBuilder()
                    .setSchema$(this.getStringOrNull(root, "$schema"))
                    .setMeta(meta)
                    .setId(root.path("id").asLong())
                    .setType(root.path("type").asText())
                    .setNamespace(root.path("namespace").asInt())
                    .setTitle(root.path("title").asText())
                    .setTitleUrl(root.path("title_url").asText())
                    .setComment(this.getStringOrNull(root, "comment"))
                    .setTimestamp(root.path("timestamp").asLong())
                    .setUser(root.path("user").asText())
                    .setBot(root.path("bot").asBoolean(false))
                    .setNotifyUrl(this.getStringOrNull(root, "notify_url"))
                    .setServerUrl(root.path("server_url").asText())
                    .setServerName(root.path("server_name").asText())
                    .setServerScriptPath(root.path("server_script_path").asText())
                    .setWiki(root.path("wiki").asText())
                    .setParsedComment(this.getStringOrNull(root, "parsedcomment"))
                    .build();

        } catch (final Exception e) {
            log.error("Failed to convert JSON to WikimediaRawEvent: {}",
                    jsonString.substring(0, Math.min(200, jsonString.length())), e);
            throw new JsonToAvroMappingException("Failed to map JSON to Avro", e);
        }
    }

    /**
     * Builds the Meta object from the meta JSON node.
     *
     * @param metaNode the JSON node containing meta information
     * @return Meta Avro object
     */
    private Meta buildMeta(JsonNode metaNode) {
        // Convert dt timestamp string to Instant
        final Instant dtInstant = this.parseDateTimeToInstant(metaNode.path("dt").asText());

        return Meta.newBuilder()
                .setUri(metaNode.path("uri").asText())
                .setRequestId(metaNode.path("request_id").asText())
                .setId(metaNode.path("id").asText())
                .setDomain(metaNode.path("domain").asText())
                .setStream(metaNode.path("stream").asText())
                .setDt(dtInstant)
                .setTopic(metaNode.path("topic").asText())
                .setPartition(metaNode.path("partition").asInt())
                .setOffset(metaNode.path("offset").asLong())
                .build();
    }

    /**
     * Gets a string value from JSON node or returns null if missing/null.
     *
     * @param node      the JSON node
     * @param fieldName the field name
     * @return the string value or null
     */
    private String getStringOrNull(JsonNode node, String fieldName) {
        final JsonNode fieldNode = node.path(fieldName);
        if (fieldNode.isMissingNode() || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asText();
    }

    /**
     * Parses ISO 8601 datetime string to Instant.
     *
     * @param dateTimeString the datetime string (e.g., "2024-01-20T10:30:45Z")
     * @return Instant
     */
    private Instant parseDateTimeToInstant(String dateTimeString) {
        try {
            return Instant.parse(dateTimeString);
        } catch (final Exception e) {
            log.warn("Failed to parse datetime string: {}, using current time", dateTimeString);
            return Instant.now();
        }
    }

}
