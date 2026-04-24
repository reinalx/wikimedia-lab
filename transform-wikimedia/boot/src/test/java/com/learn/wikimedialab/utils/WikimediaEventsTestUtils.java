package com.learn.wikimedialab.utils;

import com.wikimedia.avro.Meta;
import com.wikimedia.avro.WikimediaRawEvent;

import java.time.Instant;

public class WikimediaEventsTestUtils {

  public static WikimediaRawEvent validEvent(
      String id,
      boolean bot,
      String type,
      String domain
  ) {
    final Meta meta = Meta.newBuilder()
        .setUri("uri-" + id)
        .setRequestId("request-" + id)
        .setId(id)
        .setDomain(domain)
        .setStream("mediawiki.recentchange")
        .setDt(Instant.now())
        .setTopic("eqiad.mediawiki.recentchange")
        .setPartition(0)
        .setOffset(Long.parseLong(id))
        .build();

    return WikimediaRawEvent.newBuilder()
        .setSchema$("https://schema.wikimedia.org/test")
        .setMeta(meta)
        .setId(Long.parseLong(id))
        .setType(type)
        .setNamespace(0)
        .setTitle("title-" + id)
        .setTitleUrl("url-" + id)
        .setComment("comment-" + id)
        .setTimestamp(System.currentTimeMillis())
        .setUser("user-" + id)
        .setBot(bot)
        .setNotifyUrl(null)
        .setServerUrl("https://" + domain)
        .setServerName(domain)
        .setServerScriptPath("/w")
        .setWiki(domain.split("\\.")[0] + "wiki")
        .setParsedComment("<span>comment-" + id + "</span>")
        .build();
  }

}
