package com.learn.wikimedialab.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learn.wikimedialab.domain.values.SentimentType;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Record representing an event analysis creation.
 */
@Builder(toBuilder = true)
@Jacksonized
public record EventAnalysisCreated(
    @JsonProperty("id") String id,
    @JsonProperty("eventId") String eventId,
    @JsonProperty("analysis") String analysis,
    @JsonProperty("userId") String userId,
    @JsonProperty("sentiment") SentimentType sentiment,
    @JsonProperty("createdEventAt") OffsetDateTime createdEventAt
) {

}
