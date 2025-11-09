package com.learn.wikimedialab.domain.entities.events;

import com.learn.wikimedialab.domain.values.SentimentType;
import lombok.Builder;

/**
 * Placeholder class for EventAnalysis entity.
 */
@Builder(toBuilder = true)
public record EventAnalysis(
    String id,
    String eventId,
    String analysis,
    String userId,
    SentimentType sentiment
) {

}
