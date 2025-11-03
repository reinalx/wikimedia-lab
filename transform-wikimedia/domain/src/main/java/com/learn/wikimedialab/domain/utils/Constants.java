package com.learn.wikimedialab.domain.utils;

import lombok.experimental.UtilityClass;

/**
 * Utility class for application constants.
 */
@UtilityClass
public class Constants {

  /**
   * Wikimedia filtered Kafka topic constant.
   */
  public static final String WIKIMEDIA_FILTERED_KAFKA_TOPIC = "wikimedia.filtered.events";

  /**
   * Spanish Wikipedia domain constant.
   */
  public static final String SPANISH_WIKIPEDIA_DOMAIN = "es.wikipedia.org";

}
