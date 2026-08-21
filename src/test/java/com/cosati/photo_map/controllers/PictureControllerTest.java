package com.cosati.photo_map.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.cosati.photo_map.dto.PictureDTO;
import com.cosati.photo_map.dto.UserDTO;
import com.cosati.photo_map.exceptions.ResourceNotFoundException;
import com.cosati.photo_map.repository.PictureRepository;
import com.cosati.photo_map.repository.UserRepository;
import com.cosati.photo_map.security.JwtService;
import com.cosati.photo_map.service.PictureService;

@WebMvcTest(PictureController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PictureControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PictureRepository repository;

  @MockBean private PictureService pictureService;

  @MockBean private UserRepository userRepository;

  @MockBean private JwtService jwtService;

  @Test
  void getPicturesForUser_accessibleMap_shouldReturnPictures() throws Exception {
    UserDTO owner = new UserDTO(UUID.randomUUID(), "jane");
    PictureDTO picture = new PictureDTO(UUID.randomUUID(), "Paris", "", null, null, null, null, owner);
    when(pictureService.getPicturesForUsername("jane")).thenReturn(List.of(picture));

    mockMvc
        .perform(get("/users/jane/pictures"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(picture.getId().toString()))
        .andExpect(jsonPath("$[0].title").value("Paris"))
        .andExpect(jsonPath("$[0].user.username").value("jane"));

    verify(pictureService).getPicturesForUsername(eq("jane"));
  }

  @Test
  void getPicturesForUser_privateOrUnknownUsername_shouldReturnNotFound() throws Exception {
    when(pictureService.getPicturesForUsername("jane"))
        .thenThrow(new ResourceNotFoundException("Map not found."));

    mockMvc
        .perform(get("/users/jane/pictures"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Map not found."));
  }
}
