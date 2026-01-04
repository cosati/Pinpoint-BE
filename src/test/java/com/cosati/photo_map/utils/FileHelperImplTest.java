package com.cosati.photo_map.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileHelperImplTest {
  
  private static final String FOLDER_PATH = "some/test/path";
  private static final String FILE_NAME = "image";
  private static final String FILE_EXTENSION = ".png";
  private static final String ORIGINAL_FILE_NAME = FILE_NAME + FILE_EXTENSION;
  private static final String FILE_TYPE = "image/png";
  
  private static final Path PATH = Path.of(FOLDER_PATH);
  private static final Path RESOLVED_FOLDER_PATH = Path.of("some/test/path/" + FILE_NAME);
  
  private static final MockMultipartFile MULTIPART_FILE =
      new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME, FILE_TYPE, new byte[10]);
  
  @Mock private FileSystemHelper fileSystemHelper;
  
  private FileHelperImpl fileHelper;
  
  @BeforeEach
  void setup() {
    fileHelper = new FileHelperImpl(fileSystemHelper);
  }
  
  @Test
  void getDirectory_directoryExists_returnsInstance() throws IOException {
    when(fileSystemHelper.getPath(FOLDER_PATH)).thenReturn(PATH);
    when(fileSystemHelper.exists(PATH)).thenReturn(true);
    
    FileHelper fileHelperResult = fileHelper.getDirectory(FOLDER_PATH);

    verifyNoMoreInteractions(fileSystemHelper);
    assertThat(fileHelperResult).isEqualTo(fileHelper);
  }
  
  @Test
  void getDirectory_directoryDoesNotExist_returnsInstance() throws IOException {
    when(fileSystemHelper.getPath(FOLDER_PATH)).thenReturn(PATH);
    when(fileSystemHelper.exists(PATH)).thenReturn(false);
    
    var fileHelperResult = fileHelper.getDirectory(FOLDER_PATH);

    verify(fileSystemHelper, times(1)).createDirectories(PATH);
    assertThat(fileHelperResult).isEqualTo(fileHelper);
  }
  
  @Test
  void writeFile_whenSucessful_writesFile_returnsFilePath() throws IOException {
    when(fileSystemHelper.getPath(FOLDER_PATH)).thenReturn(PATH);
    when(fileSystemHelper.exists(PATH)).thenReturn(true);
    
    var fileHelperTest = fileHelper.getDirectory(FOLDER_PATH);
    var result = fileHelperTest.writeFile(MULTIPART_FILE, FILE_NAME);
    
    verify(fileSystemHelper, times(1)).writeFile(RESOLVED_FOLDER_PATH, MULTIPART_FILE.getBytes());
    assertThat(result).isEqualTo(RESOLVED_FOLDER_PATH);
  }
  
  @Test
  void writeFile_uploadPathIsNull_ThrowsIllegalStateException() {
    assertThatThrownBy(() -> fileHelper.writeFile(MULTIPART_FILE, FILE_NAME))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Directory not created. Call createDirectory first.");
    
    verifyNoMoreInteractions(fileSystemHelper);
  }
  
}
