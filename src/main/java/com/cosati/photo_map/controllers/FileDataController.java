package com.cosati.photo_map.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cosati.photo_map.service.FileDataService;

@RestController
@CrossOrigin
@RequestMapping("/fileData")
public class FileDataController {

  private FileDataService fileDataService;

  private final String foldePath;
  
  @Autowired
  public FileDataController(
      FileDataService fileDataService,
      @Value("${folder.path}") String folderPath) {
    this.fileDataService = fileDataService;
    this.foldePath = folderPath;
  }

  @GetMapping("/images/{filename}")
  public ResponseEntity<FileUrlResource> getImage(@PathVariable("filename") String filename) {
    return fileDataService.getFileUrlResourceResponse(filename, foldePath);
  }
}
