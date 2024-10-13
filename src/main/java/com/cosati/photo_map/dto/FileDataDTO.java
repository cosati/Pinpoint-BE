package com.cosati.photo_map.dto;

import java.util.Objects;

import lombok.Builder;

@Builder
public class FileDataDTO {
  private long id;
  private String filename;
  private String filePath;
  private String url;

  public FileDataDTO(long id, String fileName, String filePath, String apiResource) {
    this.id = id;
    this.filename = fileName;
    this.filePath = filePath;
    this.url = apiResource + this.filename;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public int hashCode() {
    return Objects.hash(filePath, filename, id, url);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    FileDataDTO other = (FileDataDTO) obj;
    return Objects.equals(filePath, other.filePath)
        && Objects.equals(filename, other.filename)
        && id == other.id
        && Objects.equals(url, other.url);
  }
}
