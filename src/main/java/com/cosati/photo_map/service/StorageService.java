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
import com.cosati.photo_map.utils.UUIDGenerator;

@Service
public class StorageService {

  @Autowired private FileDataRepository fileDataRepository;

  @Autowired private UUIDGenerator uuidGenerator;

  @Value("${folder.path}")
  private String folderPath;

  public FileData uploadImageToFileSystem(MultipartFile file) throws IOException {
    String originalFileName = file.getOriginalFilename();
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
    String uniqueFileName = uuidGenerator.generateUUID() + fileExtension;

    Path uploadPath = Paths.get(folderPath);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    Path filePath = uploadPath.resolve(uniqueFileName);
    Files.write(filePath, file.getBytes());

    FileData fileData =
        fileDataRepository.save(
            new FileData(filePath.toString(), file.getContentType(), uniqueFileName));

    if (fileData != null) {
      return fileData;
    }
    return null;
  }

  public FileDataDTO convertToDTO(FileData fileData) {
    return new FileDataDTO(
        fileData.getId(), fileData.getName(), fileData.getFilePath(), "/fileData/images/");
  }
}
