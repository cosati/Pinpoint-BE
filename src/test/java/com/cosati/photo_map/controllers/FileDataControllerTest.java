package com.cosati.photo_map.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.cosati.photo_map.service.FileDataService;

@WebMvcTest(FileDataController.class)
public class FileDataControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private FileDataService fileDataService;
  
  @Test
  void getImage_ifExists_shouldReturnImage() throws Exception {
    FileUrlResource resource = mock(FileUrlResource.class);
    ResponseEntity<FileUrlResource> response = ResponseEntity.ok(resource);
    when(fileDataService.getFileUrlResourceResponse(eq("test.png"), anyString()))
      .thenReturn(response);
    
    mockMvc.perform(get("/fileData/images/test.png"))
      .andExpect(status().isOk());
    
    verify(fileDataService)
      .getFileUrlResourceResponse(eq("test.png"), anyString());
  }
  
}
