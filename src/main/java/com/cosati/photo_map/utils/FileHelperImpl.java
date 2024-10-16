package com.cosati.photo_map.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHelperImpl implements FileHelper {

  private Path uploadPath;

  @Override
  public FileHelper getDirectory(String folderPath) throws IOException {
    uploadPath = Paths.get(folderPath);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }
    return this;
  }

  @Override
  public Path writeFile(MultipartFile file, String uniqueFileName) throws IOException {
    if (this.uploadPath == null) {
      throw new IllegalStateException("Directory not created. Call createDirectory first.");
    }
    Path filePath = uploadPath.resolve(uniqueFileName);
    Files.write(filePath, file.getBytes());
    return filePath;
  }
}
