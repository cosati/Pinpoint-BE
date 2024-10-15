package com.cosati.photo_map.utils;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class DefaultUUIDGenerator implements UUIDGenerator {

  @Override
  public String generateUUID() {
    return UUID.randomUUID().toString();
  }
}
