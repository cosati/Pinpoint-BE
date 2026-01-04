package com.cosati.photo_map.service;

import java.io.FileNotFoundException;
import java.io.IOException;
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
  
  private FileUrlResource getFileUrlResource(String filename, String folderPath) 
      throws FileNotFoundException, IOException {
      Path filePath = fileSystemHelper
          .getPath(folderPath)
          .resolve(filename)
          .normalize();
      
      FileUrlResource resource = fileSystemHelper.getResource(filePath);

      if (!resource.exists()) {
        throw new FileNotFoundException();
      }

      return resource;
  }
  
  private String getFileContentType(String filename, String folderPath) throws IOException {
    Path filePath = fileSystemHelper.getPath(folderPath).resolve(filename).normalize();
    return fileSystemHelper.probeContentType(filePath);
  }
  
  public ResponseEntity<FileUrlResource> getFileUrlResourceResponse(String filename, String folderPath) {
    try {
      var fileUrlResource = getFileUrlResource(filename, folderPath);
      var contentType = getFileContentType(filename, folderPath);
      
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + fileUrlResource.getFilename() + "\"")
          .body(fileUrlResource);
    } catch (FileNotFoundException e) {
      return ResponseEntity.status(404).body(null);
    } catch (IOException e) {
      return ResponseEntity.status(500).body(null);
    }
  }
}
