package com.learn.wikimedialab.domain.entities;

import lombok.Builder;

/**
 * Record representing JWT information.
 */
@Builder(toBuilder = true)
public record JwtInfo(
    String userId,
    String username,
    String role
) {

}
