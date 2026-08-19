package com.cosati.photo_map.dto;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class PictureDTO {
  private UUID id;
  private String title;
  private String description;
  private Date dateTaken;
  private GeolocationDTO geolocation;
  private FileDataDTO fileData;
  private PinDTO pin;
  private UserDTO user;

  public PictureDTO(
      UUID id,
      String title,
      String description,
      Date dateTaken,
      GeolocationDTO geolocation,
      FileDataDTO fileData,
      PinDTO pin,
      UserDTO user) {
    super();
    this.id = id;
    this.title = title;
    this.description = description;
    this.dateTaken = dateTaken;
    this.geolocation = geolocation;
    this.fileData = fileData;
    this.pin = pin;
    this.user = user;
  }

  public UserDTO getUser() {
  return user;}

  public void setUser(UserDTO user) {
  this.user = user;}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setName(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getDateTaken() {
    return dateTaken;
  }

  public void setDateTaken(Date dateTaken) {
    this.dateTaken = dateTaken;
  }

  public GeolocationDTO getGeolocation() {
    return geolocation;
  }

  public void setGeolocation(GeolocationDTO geolocation) {
    this.geolocation = geolocation;
  }

  public FileDataDTO getFileData() {
    return fileData;
  }

  public void setFileData(FileDataDTO fileData) {
    this.fileData = fileData;
  }

  public PinDTO getPin() {
    return pin;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateTaken, description, fileData, geolocation, id, pin, title, user);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    PictureDTO other = (PictureDTO) obj;
    return Objects.equals(dateTaken, other.dateTaken)
        && Objects.equals(description, other.description)
        && Objects.equals(fileData, other.fileData)
        && Objects.equals(geolocation, other.geolocation)
        && id == other.id
        && Objects.equals(pin, other.pin)
        && Objects.equals(title, other.title)
        && Objects.equals(user, other.user);
  }
}
