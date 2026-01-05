package com.cosati.photo_map.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cosati.photo_map.domain.Pin;
import com.cosati.photo_map.repository.PinRepository;

@ExtendWith(MockitoExtension.class)
public class PinServiceTest {
  
  @TempDir Path tempDir;
  
  @Mock private PinRepository pinRepository;
  
  private PinService pinService;
  
  @BeforeEach
  void setup() {
    pinService = new PinService(tempDir.toString(), pinRepository);
  }
  
  @Test
  void convertToDTO_shouldReturnDTO() {
    String color = "BLUE";
    String fileName = "blue.svg";
    Pin pin = new Pin(color, fileName);
    
    var pinDto = pinService.convertToDTO(pin);
    
    assertThat(pinDto.getColor()).isEqualTo(color);
    assertThat(pinDto.getFileName()).isEqualTo(fileName);
    assertThat(pinDto.getUrl()).isEqualTo("/pins/icons/" + fileName);
  }
  
  @Test
  void updatePins_fileDoesNotExist_shouldThrowIllegalArgumentException() {
    PinService service = new PinService("non-existing-folder", pinRepository);
    
    assertThatThrownBy(() -> service.updatePins())
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Cannot find folder path.");
  }
  
  @Test
  void updatePins_fileAlreadyExists_shouldNotSavePin() throws IOException {
    Files.createFile(tempDir.resolve("blue.png"));
    when(pinRepository.findByFileName("blue.png"))
      .thenReturn(Optional.of(new Pin("BLUE", "blue.svg")));
    
    pinService.updatePins();
    
    verifyNoMoreInteractions(pinRepository);
  }
  
  @Test
  void updatePins_fileDoesNotExist_shouldSavePin() throws IOException {
    Files.createFile(tempDir.resolve("red.png"));
    when(pinRepository.findByFileName("red.png"))
      .thenReturn(Optional.empty());
    
    pinService.updatePins();
    
    ArgumentCaptor<Pin> captor = ArgumentCaptor.forClass(Pin.class);
    verify(pinRepository).save(captor.capture());
    Pin savedPin = captor.getValue();
    assertThat(savedPin.getColor()).isEqualTo("RED");
    assertThat(savedPin.getFileName()).isEqualTo("red.png");
  }
}
