package com.cosati.photo_map.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import com.cosati.photo_map.domain.FileData;
import com.cosati.photo_map.dto.FileDataDTO;
import com.cosati.photo_map.repository.FileDataRepository;
import com.cosati.photo_map.utils.FileHelper;
import com.cosati.photo_map.utils.UUIDGenerator;

public class StorageServiceTest {

  private static final int FILE_DATA_ID = 101;

  private static final String FILE_EXTENSION = ".png";
  private static final String DEFAULT_UUID = "123e4567-e89b-12d3-a456-426614174000";
  private static final String UUID_FILE_NAME = DEFAULT_UUID + FILE_EXTENSION;
  private static final String FILE_TYPE = "image/png";
  private static final String FILE_NAME = "image";
  private static final String ORIGINAL_FILE_NAME = FILE_NAME + FILE_EXTENSION;
  private static final String FILE_DATA_PATH = "file/path";

  private static final MockMultipartFile MULTIPART_FILE =
      new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME, FILE_TYPE, new byte[10]);

  @Mock private FileDataRepository fileDataRepository;

  @Mock private UUIDGenerator uuidGenerator;

  @Mock private FileHelper fileHelper;

  @Value("${folder.path}")
  private String folderPath = "testFolder";

  @InjectMocks private StorageService storageService;

  @BeforeEach
  void setup() throws IOException {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void uploadImageToFileSystem_whenSuccessful_shouldSaveExpectedFileData() throws IOException {
    Path mockPath = Paths.get(folderPath);
    FileData mockFileData = new FileData(mockPath.toString(), FILE_TYPE, UUID_FILE_NAME);
    when(fileHelper.getDirectory(any())).thenReturn(fileHelper);
    when(fileHelper.writeFile(MULTIPART_FILE, UUID_FILE_NAME)).thenReturn(mockPath);
    when(uuidGenerator.generateUUID()).thenReturn(DEFAULT_UUID);
    when(fileDataRepository.save(any(FileData.class))).thenReturn(mockFileData);

    storageService.uploadImageToFileSystem(MULTIPART_FILE);

    verify(fileDataRepository).save(mockFileData);
  }

  @Test
  void uploadImageToFileSystem_whenSucessful_returnsExpectedFileData() throws IOException {
    Path mockPath = Paths.get(folderPath);
    when(fileHelper.getDirectory(any())).thenReturn(fileHelper);
    when(fileHelper.writeFile(MULTIPART_FILE, UUID_FILE_NAME)).thenReturn(mockPath);
    when(uuidGenerator.generateUUID()).thenReturn(DEFAULT_UUID);
    FileData fileData =
        FileData.builder()
            .name(UUID_FILE_NAME)
            .type(FILE_TYPE)
            .filePath(mockPath.toString())
            .build();
    when(fileDataRepository.save(any(FileData.class))).thenReturn(fileData);

    FileData result = storageService.uploadImageToFileSystem(MULTIPART_FILE);

    verify(fileHelper).writeFile(MULTIPART_FILE, UUID_FILE_NAME);
    verify(fileDataRepository).save(fileData);
    assertEquals(UUID_FILE_NAME, result.getName());
    assertEquals(mockPath.toString(), result.getFilePath());
    assertEquals(FILE_TYPE, result.getType());
  }

  @Test
  void uploadImageToFileSystem_whenErrorGettingDirectory_shouldThrowIOException()
      throws IOException {
    when(uuidGenerator.generateUUID()).thenReturn(DEFAULT_UUID);
    when(fileHelper.getDirectory(any())).thenThrow(new IOException());

    assertThrows(IOException.class, () -> storageService.uploadImageToFileSystem(MULTIPART_FILE));

    verify(fileDataRepository, never()).save(any());
  }

  @Test
  void convertToDTO_shouldReturnExpectedDTO() {
    FileDataDTO expectedDTO =
        FileDataDTO.builder()
            .filename(UUID_FILE_NAME)
            .filePath(FILE_DATA_PATH)
            .id(FILE_DATA_ID)
            .url("/fileData/images/")
            .build();
    FileData fileData =
        FileData.builder()
            .id(FILE_DATA_ID)
            .name(UUID_FILE_NAME)
            .type(FILE_TYPE)
            .filePath(FILE_DATA_PATH)
            .build();

    FileDataDTO fileDataDTO = storageService.convertToDTO(fileData);

    assertEquals(expectedDTO, fileDataDTO);
  }
}
