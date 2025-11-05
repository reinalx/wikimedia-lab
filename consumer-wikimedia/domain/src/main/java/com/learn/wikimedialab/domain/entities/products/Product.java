package com.learn.wikimedialab.domain.entities.products;

import lombok.Builder;

@Builder(toBuilder = true)
public record Product(
    Long id,
    String name,
    String description,
    double price
) {
}
