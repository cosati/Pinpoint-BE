package com.cosati.photo_map.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cosati.photo_map.domain.FileData;
import com.cosati.photo_map.domain.Geolocation;
import com.cosati.photo_map.domain.Picture;
import com.cosati.photo_map.dto.FileDataDTO;
import com.cosati.photo_map.dto.GeolocationDTO;
import com.cosati.photo_map.dto.PictureDTO;
import com.cosati.photo_map.dto.PinDTO;
import com.cosati.photo_map.repository.PictureRepository;

@Service
public class PictureService {

  @Autowired private PictureRepository pictureRepository;

  @Autowired private StorageService storageService;

  @Autowired private PinService pinService;

  public Picture savePictureWithImage(Picture picture, MultipartFile file) {
    try {
      FileData fileData = storageService.uploadImageToFileSystem(file);
      picture.setFileData(fileData);
    } catch (IOException e) {
      return null;
    }
    return pictureRepository.save(picture);
  }

  public Picture updatePicture(Picture picture) {
    Picture pictureToUpdate =
        pictureRepository
            .findById(picture.getId())
            .orElseThrow(() -> new RuntimeException("Picture not found."));
    pictureToUpdate.setDateTaken(picture.getDateTaken());
    pictureToUpdate.setDescription(picture.getDescription());
    pictureToUpdate.setPin(picture.getPin());
    pictureToUpdate.setTitle(picture.getTitle());
    return pictureRepository.save(pictureToUpdate);
  }

  public PictureDTO convertToDTO(Picture picture) {
    Geolocation geolocation = picture.getGeolocation();
    GeolocationDTO geolocationDTO =
        new GeolocationDTO(
            geolocation.getId(), geolocation.getLongitude(), geolocation.getLatitude());

    FileDataDTO fileDataDto = storageService.convertToDTO(picture.getFileData());

    PinDTO pinDto = picture.getPin() != null ? pinService.convertToDTO(picture.getPin()) : null;

    return new PictureDTO(
        picture.getId(),
        picture.getTitle(),
        picture.getDescription(),
        picture.getDateTaken(),
        geolocationDTO,
        fileDataDto,
        pinDto);
  }
}
