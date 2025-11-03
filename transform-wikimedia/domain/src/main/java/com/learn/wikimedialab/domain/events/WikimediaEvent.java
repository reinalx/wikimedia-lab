package com.learn.wikimedialab.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record representing a Wikimedia event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WikimediaEvent(
    String id,
    String type,
    String user,
    boolean bot,
    MetaInfo meta
) {

}
