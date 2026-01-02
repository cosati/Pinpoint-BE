package com.cosati.photo_map.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileHelperImplTest {
  
  private String FOLDER_PATH = "some/test/path";
  private Path PATH = Path.of(FOLDER_PATH);
  
  @Mock private FileSystemHelper fileSystemHelper;
  
  private FileHelperImpl fileHelper;
  
  @BeforeEach
  void setup() {
    fileHelper = new FileHelperImpl(fileSystemHelper);
  }
  
//  @Test
//  void getDirectory_directoryExists_returnsInstance() throws IOException {
//    when(fileSystemHelper.getPath(FOLDER_PATH)).thenReturn(PATH);
//    when(fileSystemHelper.exists(PATH)).thenReturn(true);
//    
//    FileHelper fileHelper = fileHelper.getDirectory(FOLDER_PATH);
//
//    verifyNoMoreInteractions(fileSystemHelper);
//    assertThat(fileHelper).isEqualTo(fileHelper);
//  }
  
//  @Test
//  void getDirectory_directoryDoesNotExist_returnsInstance() throws IOException {
//    when(fileSystemHelper.getPath(FOLDER_PATH)).thenReturn(PATH);
//    when(fileSystemHelper.exists(PATH)).thenReturn(true);
//    
//    var unused = fileHelper.getDirectory(FOLDER_PATH);
//
//    verifyNoMoreInteractions(fileSystemHelper);
//  }
}
