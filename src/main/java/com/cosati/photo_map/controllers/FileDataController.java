package com.cosati.photo_map.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/fileData")
public class FileDataController {

  private final String FOLDER_PATH = "C:/Users/Cosati/Documents/mapImages";

  @GetMapping("/images/{filename}")
  public ResponseEntity<FileUrlResource> getImage(@PathVariable("filename") String filename) {
    try {
      Path filePath = Paths.get(FOLDER_PATH).resolve(filename).normalize();
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
