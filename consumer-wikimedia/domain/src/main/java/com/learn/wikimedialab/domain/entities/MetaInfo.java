package com.learn.wikimedialab.domain.entities;

/**
 * Record representing metadata associated with a Wikimedia event.
 */
public record MetaInfo(
    String domain,
    String uri,
    String dt) {

}
