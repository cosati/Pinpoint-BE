package com.cosati.photo_map.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import org.springframework.core.io.FileUrlResource;

public interface FileSystemHelper {
  boolean exists(Path path);
  void createDirectories(Path path) throws IOException;
  Path getPath(String folderPath);
  void writeFile(Path filePath, byte[] file) throws IOException;
  String probeContentType(Path filePath) throws IOException;
  FileUrlResource getResource(Path filePath) throws MalformedURLException;
}
