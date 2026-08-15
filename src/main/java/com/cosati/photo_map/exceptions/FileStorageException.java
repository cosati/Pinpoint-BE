package com.cosati.photo_map.exceptions;

public class FileStorageException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public FileStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
