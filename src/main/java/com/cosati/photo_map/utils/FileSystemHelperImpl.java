package com.cosati.photo_map.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemHelperImpl implements FileSystemHelper {

  @Override
  public boolean exists(Path path) {
    return Files.exists(path);
  }

  @Override
  public void createDirectories(Path path) throws IOException {
    Files.createDirectories(path);
  }

  @Override
  public Path getPath(String folderPath) {
    return Paths.get(folderPath);
  }
  
}
