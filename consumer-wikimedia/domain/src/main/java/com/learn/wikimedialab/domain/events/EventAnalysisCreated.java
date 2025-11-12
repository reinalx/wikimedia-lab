package com.learn.wikimedialab.domain.events;

import com.learn.wikimedialab.domain.values.SentimentType;
import java.time.OffsetDateTime;

/**
 * Record representing an event analysis creation.
 */
public record EventAnalysisCreated(
    String id,
    String eventId,
    String analysis,
    String userId,
    SentimentType sentiment,
    OffsetDateTime createdEventAt
) {

}
