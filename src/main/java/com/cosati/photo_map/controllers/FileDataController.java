package com.cosati.photo_map.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
  
  // TODO fix
  private final String FOLDER_PATH = "C:/Users/Cosati/OneDrive/Documentos/mapImages";
  
  @Autowired
  public FileDataController(FileDataService fileDataService) {
    this.fileDataService = fileDataService;
  }

  @GetMapping("/images/{filename}")
  public ResponseEntity<FileUrlResource> getImage(@PathVariable("filename") String filename) {
    return fileDataService.getFileUrlResourceResponse(filename, FOLDER_PATH);
  }
}
