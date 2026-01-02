package com.cosati.photo_map.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class DefaultUUIDGeneratorTest {
  
  UUIDGenerator defaultUUIDGenerator = new DefaultUUIDGenerator();
  
  @Test
  void generateUUID_returnsUUID() {
    var randomUUID = defaultUUIDGenerator.generateUUID();
    
    assertThat(randomUUID).isNotNull();
    assertThat(randomUUID)
      .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
  }
}
