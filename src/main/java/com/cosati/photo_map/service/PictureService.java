package com.cosati.photo_map.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cosati.photo_map.domain.FileData;
import com.cosati.photo_map.domain.Geolocation;
import com.cosati.photo_map.domain.Picture;
import com.cosati.photo_map.domain.User;
import com.cosati.photo_map.dto.FileDataDTO;
import com.cosati.photo_map.dto.GeolocationDTO;
import com.cosati.photo_map.dto.PictureDTO;
import com.cosati.photo_map.dto.PinDTO;
import com.cosati.photo_map.dto.UserDTO;
import com.cosati.photo_map.exceptions.ForbiddenOperationException;
import com.cosati.photo_map.exceptions.ResourceNotFoundException;
import com.cosati.photo_map.repository.PictureRepository;
import com.cosati.photo_map.repository.UserRepository;

@Service
public class PictureService {

  private static final String MAP_NOT_FOUND_MESSAGE = "Map not found.";

  @Autowired private PictureRepository pictureRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private StorageService storageService;

  @Autowired private PinService pinService;

  @Autowired private AuthenticatedUserProvider userProvider;

  public List<PictureDTO> getPicturesForUsername(String username) {
    User owner =
        userRepository
            .findByDisplayName(username)
            .orElseThrow(() -> new ResourceNotFoundException(MAP_NOT_FOUND_MESSAGE));

    boolean isOwner =
        userProvider
            .getCurrentUserOptional()
            .map(currentUser -> currentUser.getId().equals(owner.getId()))
            .orElse(false);

    if (!owner.isPublic() && !isOwner) {
      // Same exception/message as "username doesn't exist" above, so the
      // response can't be used to tell private maps and unregistered
      // usernames apart.
      throw new ResourceNotFoundException(MAP_NOT_FOUND_MESSAGE);
    }

    return pictureRepository.findByUser(owner).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public Picture savePictureWithImage(Picture picture, MultipartFile file) {
    picture.setUser(userProvider.getCurrentUser());
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
            .orElseThrow(() -> new ResourceNotFoundException("Picture not found."));
    
    assertOwnership(pictureToUpdate);
    
    pictureToUpdate.setDateTaken(picture.getDateTaken());
    pictureToUpdate.setDescription(picture.getDescription());
    pictureToUpdate.setPin(picture.getPin());
    pictureToUpdate.setTitle(picture.getTitle());
    return pictureRepository.save(pictureToUpdate);
  }
  
  public void deletePicture(UUID id) {
    Picture picture =
        pictureRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Picture not found."));

    assertOwnership(picture);

    pictureRepository.deleteById(id);
  }

  public PictureDTO convertToDTO(Picture picture) {
    Geolocation geolocation = picture.getGeolocation();
    GeolocationDTO geolocationDTO =
        new GeolocationDTO(
            geolocation.getId(), geolocation.getLongitude(), geolocation.getLatitude());

    FileDataDTO fileDataDto = storageService.convertToDTO(picture.getFileData());

    PinDTO pinDto = picture.getPin() != null ? pinService.convertToDTO(picture.getPin()) : null;
    
    UserDTO user = new UserDTO(picture.getUser().getId(), picture.getUser().getDisplayName());

    return new PictureDTO(
        picture.getId(),
        picture.getTitle(),
        picture.getDescription(),
        picture.getDateTaken(),
        geolocationDTO,
        fileDataDto,
        pinDto,
        user);
  }
  
  private void assertOwnership(Picture picture) {
    User currentUser = userProvider.getCurrentUser();
    if (!picture.getUser().getId().equals(currentUser.getId())) {
      throw new ForbiddenOperationException("You do not have permission to modify this picture.");
    }
  }
}
