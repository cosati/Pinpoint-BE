package com.cosati.photo_map.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.cosati.photo_map.domain.FileData;
import com.cosati.photo_map.domain.Geolocation;
import com.cosati.photo_map.domain.Picture;
import com.cosati.photo_map.domain.Pin;
import com.cosati.photo_map.dto.FileDataDTO;
import com.cosati.photo_map.dto.GeolocationDTO;
import com.cosati.photo_map.dto.PictureDTO;
import com.cosati.photo_map.dto.PinDTO;
import com.cosati.photo_map.repository.PictureRepository;

public class PictureServiceTest {

  private static final long DEFAULT_TIMESTAMP = 1672531200000L;
  private static final Date DEFAULT_DATE = new Date(DEFAULT_TIMESTAMP);

  private static final int DEFAULT_FILE_DATA_ID = 1;
  private static final int DEFAULT_GEOLOCATION_ID = 0;
  private static final int DEFAULT_POST_ID = 100;
  private static final int PIN_ID = 10;

  private static final String PIN_FILE_NAME = "blue.svg";
  private static final String PIN_COLOR = "BLUE";
  private static final String DEFAULT_POST_DESCRIPTION = "Testing...";
  private static final String DEFAULT_POST_TITLE = "Paris";
  private static final String DEFAULT_FILE_TYPE = "image/png";
  private static final String DEFAULT_API_RESOURCE = "api/resource";
  private static final String DEFAULT_FILE_PATH = "directory/path";
  private static final String DEFAULT_FILE_NAME = "file name";

  private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(2.29);
  private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(48.85);

  private static final MultipartFile file =
      new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);

  @Mock private StorageService storageService;

  @Mock private PinService pinService;

  @Mock private PictureRepository pictureRepository;

  @InjectMocks private PictureService pictureService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void savePictureWithImage_savesToFileSystem_returnsPicture() throws IOException {
    FileData fileData = new FileData(DEFAULT_FILE_PATH, DEFAULT_FILE_TYPE, DEFAULT_FILE_NAME);
    Picture picture =
        Picture.builder()
            .id(DEFAULT_POST_ID)
            .title(DEFAULT_POST_TITLE)
            .description(DEFAULT_POST_DESCRIPTION)
            .dateTaken(DEFAULT_DATE)
            .geolocation(
                Geolocation.builder()
                    .id(DEFAULT_GEOLOCATION_ID)
                    .latitude(DEFAULT_LATITUDE)
                    .longitude(DEFAULT_LONGITUDE)
                    .build())
            .build();
    when(storageService.uploadImageToFileSystem(file)).thenReturn(fileData);
    when(pictureRepository.save(picture)).thenReturn(picture);

    Picture savedPicture = pictureService.savePictureWithImage(picture, file);

    verify(storageService).uploadImageToFileSystem(file);
    verify(pictureRepository).save(picture);
    assertEquals(savedPicture, picture);
    assertEquals(fileData, savedPicture.getFileData());
  }

  @Test
  void savePictureWithImage_failsSaveToFileSystem_returnsNull() throws IOException {
    when(storageService.uploadImageToFileSystem(any()))
        .thenThrow(new IOException("File upload Error"));
    Picture picture =
        Picture.builder()
            .id(DEFAULT_POST_ID)
            .title(DEFAULT_POST_TITLE)
            .description(DEFAULT_POST_DESCRIPTION)
            .dateTaken(DEFAULT_DATE)
            .geolocation(
                Geolocation.builder()
                    .id(DEFAULT_GEOLOCATION_ID)
                    .latitude(DEFAULT_LATITUDE)
                    .longitude(DEFAULT_LONGITUDE)
                    .build())
            .build();

    Picture savedPicture = pictureService.savePictureWithImage(picture, file);

    assertNull(savedPicture);
    verify(storageService).uploadImageToFileSystem(file);
    verify(pictureRepository, never()).save(any());
  }

  @Test
  void convertToDTO_returnsPictureDTO() {
    FileDataDTO fileDataDTO =
        FileDataDTO.builder()
            .id(DEFAULT_FILE_DATA_ID)
            .filename(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .url(DEFAULT_API_RESOURCE)
            .build();
    when(storageService.convertToDTO(
            new FileData(DEFAULT_FILE_PATH, DEFAULT_FILE_TYPE, DEFAULT_FILE_NAME)))
        .thenReturn(fileDataDTO);
    GeolocationDTO geolocationDTO =
        new GeolocationDTO(DEFAULT_GEOLOCATION_ID, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
    Pin pin = Pin.builder().id(PIN_ID).color(PIN_COLOR).fileName(PIN_FILE_NAME).build();
    PinDTO pinDTO = new PinDTO(10, PIN_COLOR, PIN_FILE_NAME, "/pins/icons/");
    when(pinService.convertToDTO(pin)).thenReturn(pinDTO);
    PictureDTO expectedDTO =
        new PictureDTO(
            DEFAULT_POST_ID,
            DEFAULT_POST_TITLE,
            DEFAULT_POST_DESCRIPTION,
            DEFAULT_DATE,
            geolocationDTO,
            fileDataDTO,
            pinDTO);
    Picture picture =
        Picture.builder()
            .id(DEFAULT_POST_ID)
            .title(DEFAULT_POST_TITLE)
            .description(DEFAULT_POST_DESCRIPTION)
            .dateTaken(DEFAULT_DATE)
            .geolocation(
                Geolocation.builder()
                    .id(DEFAULT_GEOLOCATION_ID)
                    .latitude(DEFAULT_LATITUDE)
                    .longitude(DEFAULT_LONGITUDE)
                    .build())
            .fileData(new FileData(DEFAULT_FILE_PATH, DEFAULT_FILE_TYPE, DEFAULT_FILE_NAME))
            .pin(pin)
            .build();

    PictureDTO pictureDTO = pictureService.convertToDTO(picture);

    assertEquals(pictureDTO, expectedDTO);
  }
}
