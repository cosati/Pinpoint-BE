package com.cosati.photo_map.utils;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemHelper {
  boolean exists(Path path);
  void createDirectories(Path path) throws IOException;
  Path getPath(String folderPath);
}
