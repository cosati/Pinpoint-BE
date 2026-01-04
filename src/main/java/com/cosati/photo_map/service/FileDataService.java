package com.cosati.photo_map.service;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cosati.photo_map.utils.FileSystemHelper;

@Service
public class FileDataService {
  
  private final FileSystemHelper fileSystemHelper;
  
  @Autowired
  public FileDataService(FileSystemHelper fileSystemHelper) {
    this.fileSystemHelper = fileSystemHelper;
  }

  public ResponseEntity<FileUrlResource> getFileUrlResource(String filename, String folderPath) {
    try {
      Path filePath = fileSystemHelper.getPath(folderPath).resolve(filename).normalize();
      FileUrlResource resource = new FileUrlResource(filePath.toString());

      if (!resource.exists()) {
        return ResponseEntity.notFound().build();
      }

      String contentType = fileSystemHelper.probeContentType(filePath);

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
