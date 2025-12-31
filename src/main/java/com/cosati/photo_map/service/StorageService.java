package com.cosati.photo_map.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cosati.photo_map.domain.FileData;
import com.cosati.photo_map.dto.FileDataDTO;
import com.cosati.photo_map.repository.FileDataRepository;
import com.cosati.photo_map.utils.FileHelper;
import com.cosati.photo_map.utils.UUIDGenerator;

@Service
public class StorageService {

  private FileDataRepository fileDataRepository;

  private UUIDGenerator uuidGenerator;

  private FileHelper fileHelper;

  private String folderPath;
  
  @Autowired
  public StorageService(
      @Value("${folder.path}") String folderPath, 
      FileDataRepository fileDataRepository,
      UUIDGenerator uuidGenerator,
      FileHelper fileHelper) {
    this.folderPath = folderPath;
    this.fileDataRepository = fileDataRepository;
    this.uuidGenerator = uuidGenerator;
    this.fileHelper = fileHelper;
  }

  public FileData uploadImageToFileSystem(MultipartFile file) throws IOException {
    String originalFileName = file.getOriginalFilename();
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
    String uniqueFileName = uuidGenerator.generateUUID() + fileExtension;

    Path filePath = savetoFileSystem(file, uniqueFileName);

    FileData fileData =
        fileDataRepository.save(
            new FileData(filePath.toString(), file.getContentType(), uniqueFileName));

    if (fileData != null) {
      return fileData;
    }
    return null;
  }

  private Path savetoFileSystem(MultipartFile file, String uniqueFileName) throws IOException {
    return fileHelper.getDirectory(folderPath).writeFile(file, uniqueFileName);
  }

  public FileDataDTO convertToDTO(FileData fileData) {
    // TODO: Add DTO url to configurations.
    return new FileDataDTO(
        fileData.getId(), fileData.getName(), fileData.getFilePath(), "/fileData/images/");
  }
}
