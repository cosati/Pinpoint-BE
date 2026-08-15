package com.cosati.photo_map.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public UsernameAlreadyTakenException(String message) {
    super(message);
  }
}
