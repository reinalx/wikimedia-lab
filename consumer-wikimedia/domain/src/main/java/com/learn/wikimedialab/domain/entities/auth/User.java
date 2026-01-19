package com.learn.wikimedialab.domain.entities.auth;

import lombok.Builder;

/**
 * Record representing a User entity.
 */
@Builder(toBuilder = true)
public record User(
    String id,
    String username,
    String password,
    String role
) {

}
