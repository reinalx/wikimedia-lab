package com.learn.wikimedialab.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.OffsetDateTime;
import lombok.Builder;

/**
 * Record representing a Wikimedia event.
 */
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record WikimediaEvent(
    String id,
    String type,
    String user,
    String title,
    String titleUrl,
    String comment,
    boolean bot,
    MetaInfo meta,
    OffsetDateTime createdAt
) {

}
