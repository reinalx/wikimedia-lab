package com.learn.wikimedialab.domain.entities;

import com.learn.wikimedialab.domain.values.SentimentType;

/**
 * Placeholder class for EventAnalysis entity.
 */
public record EventAnalysis(
    String id,
    String eventId,
    String analysis,
    String userId,
    SentimentType sentiment
) {

}
