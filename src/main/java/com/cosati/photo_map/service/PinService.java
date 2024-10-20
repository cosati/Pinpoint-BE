package com.cosati.photo_map.service;

import java.io.File;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cosati.photo_map.domain.Pin;
import com.cosati.photo_map.dto.PinDTO;
import com.cosati.photo_map.repository.PinRepository;
import jakarta.annotation.PostConstruct;

@Service
public class PinService {

  private final String ICONS_LOCATION = "C:/Users/Cosati/OneDrive/Imagens/pins";

  @Autowired private PinRepository pinRepository;

  @PostConstruct
  public void updatePins() {
    File folder = new File(ICONS_LOCATION);

    if (!folder.exists() || !folder.isDirectory()) {
      throw new IllegalArgumentException("Cannot find folder path.");
    }

    File[] files = folder.listFiles();

    if (files != null) {
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          String color = fileName.substring(0, fileName.lastIndexOf('.')).toUpperCase();

          Optional<Pin> existingPin = pinRepository.findByFileName(fileName);

          if (!existingPin.isPresent()) {
            Pin newPin = new Pin(color, fileName);
            pinRepository.save(newPin);
          }
        }
      }
    }
  }

  public PinDTO convertToDTO(Pin pin) {
    return new PinDTO(pin.getId(), pin.getColor(), pin.getFileName(), "/pins/icons/");
  }
}
