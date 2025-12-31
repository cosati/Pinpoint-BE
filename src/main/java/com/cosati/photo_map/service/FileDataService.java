package com.cosati.photo_map.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FileDataService {

  public ResponseEntity<FileUrlResource> getFileUrlResource(String filename, String folderPath) {
    try {
      System.out.println("Getting directory: %s".formatted(folderPath));
      Path filePath = Paths.get(folderPath).resolve(filename).normalize();
      FileUrlResource resource = new FileUrlResource(filePath.toString());

      if (!resource.exists()) {
        return ResponseEntity.notFound().build();
      }

      String contentType = Files.probeContentType(filePath);

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + resource.getFilename() + "\"")
          .body(resource);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }
}
