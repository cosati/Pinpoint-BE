package com.cosati.photo_map.dto;

import java.util.Objects;
import java.util.UUID;

public class PinDTO {
  private UUID id;
  private String color;
  private String fileName;
  private String url;

  public PinDTO(UUID id, String color, String fileName, String url) {
    this.id = id;
    this.color = color;
    this.fileName = fileName;
    this.url = url + this.fileName;
  }
  
  public UUID getId() {
    return id;
  }

  public String getColor() {
    return color;
  }

  public String getFileName() {
    return fileName;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, fileName, id, url);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PinDTO other = (PinDTO) obj;
    return Objects.equals(color, other.color)
        && Objects.equals(fileName, other.fileName)
        && id == other.id
        && Objects.equals(url, other.url);
  }
}
