package com.cosati.photo_map.dto;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {
  private UUID id;
  private String username;
  
  public UserDTO(UUID id, String username) {
    this.id = id;
    this.username = username;
  }

  public UUID getId() {
  return id;}

  public void setId(UUID id) {
  this.id = id;}

  public String getUsername() {
  return username;}

  public void setUsername(String username) {
  this.username = username;}

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    UserDTO other = (UserDTO) obj;
    return Objects.equals(id, other.id) && Objects.equals(username, other.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }
}
