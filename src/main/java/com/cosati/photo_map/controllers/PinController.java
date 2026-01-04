package com.cosati.photo_map.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cosati.photo_map.dto.PinDTO;
import com.cosati.photo_map.repository.PinRepository;
import com.cosati.photo_map.service.FileDataService;
import com.cosati.photo_map.service.PinService;

@RestController
@CrossOrigin
@RequestMapping("/pins")
public class PinController {

  @Autowired private PinRepository pinRepository;

  @Autowired private PinService pinService;

  @Autowired private FileDataService fileDataService;

  //TODO fix
  private final String ICONS_LOCATION = "C:/Users/Cosati/OneDrive/Imagens/pins";

  @GetMapping("/icons")
  public ResponseEntity<List<PinDTO>> getPins() {
    return ResponseEntity.ok(
        pinRepository
            .findAll()
            .stream()
            .map(pinService::convertToDTO)
            .collect(Collectors.toList()));
  }

  @GetMapping("/icons/{filename}")
  public ResponseEntity<FileUrlResource> getIcon(@PathVariable("filename") String filename) {
    return fileDataService.getFileUrlResourceResponse(filename, ICONS_LOCATION);
  }
}
