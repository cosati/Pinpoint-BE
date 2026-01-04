package com.cosati.photo_map.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.cosati.photo_map.utils.FileSystemHelper;

@ExtendWith(MockitoExtension.class)
public class FileDataServiceTest {  
  private static final String FILE_NAME = "image.jpg";

  @TempDir Path tempDir;
  
  @Mock private FileSystemHelper fileSystemHelper;
  
  private Path mockPath = mock(Path.class);
  private FileUrlResource mockResource = mock(FileUrlResource.class);
  
  @InjectMocks private FileDataService fileDataService;
  
  @BeforeEach
  void setup() throws Exception {
    when(fileSystemHelper.getPath(anyString())).thenReturn(mockPath);
    when(mockPath.resolve(anyString())).thenReturn(mockPath);
    when(mockPath.normalize()).thenReturn(mockPath);
    when(mockResource.getFilename()).thenReturn(FILE_NAME);
    when(fileSystemHelper.getResource(mockPath)).thenReturn(mockResource);
  }
  
  @Test
  void getFileUrlResource_resourceExists_returnsFileUrlResource() 
      throws Exception {
    when(fileSystemHelper.probeContentType(mockPath)).thenReturn("image/jpeg");
    when(mockResource.exists()).thenReturn(true);
    
    var response = fileDataService.getFileUrlResourceResponse(FILE_NAME, "path");
    
    assertThat(response).isEqualTo(
        ResponseEntity
          .ok()
          .contentType(MediaType.IMAGE_JPEG)
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + FILE_NAME + "\"")
          .body(mockResource));
  }
  
  @Test
  void getFileUrlResource_resourceDoesNotExist_returns404() 
      throws Exception {
    when(mockResource.exists()).thenReturn(false);
    
    var response = fileDataService.getFileUrlResourceResponse(FILE_NAME, "path");
    
    assertThat(response).isEqualTo(ResponseEntity.status(404).body(null));
  }
  
  @Test
  void getFileUrlResource_iOException_returns500() 
      throws Exception {
    when(mockResource.exists()).thenReturn(true);
    when(fileSystemHelper.probeContentType(mockPath)).thenThrow(IOException.class);
    
    var response = fileDataService.getFileUrlResourceResponse(FILE_NAME, "path");
    
    assertThat(response).isEqualTo(ResponseEntity.status(500).body(null));
  }
  
}
