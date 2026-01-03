package com.cosati.photo_map.utils;

import java.io.IOException;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHelperImpl implements FileHelper {

  private Path uploadPath;
  private final FileSystemHelper fileSystemHelper;
  
  @Autowired
  public FileHelperImpl(FileSystemHelper fileSystemHelper) {
    this.fileSystemHelper = fileSystemHelper;
  }

  @Override
  public FileHelper getDirectory(String folderPath) throws IOException {
    uploadPath = fileSystemHelper.getPath(folderPath);
    if (!fileSystemHelper.exists(uploadPath)) {
      fileSystemHelper.createDirectories(uploadPath);
    }
    return this;
  }

  @Override
  public Path writeFile(MultipartFile file, String uniqueFileName) throws IOException {
    if (this.uploadPath == null) {
      throw new IllegalStateException("Directory not created. Call createDirectory first.");
    }
    Path filePath = uploadPath.resolve(uniqueFileName);
    fileSystemHelper.writeFile(filePath, file.getBytes());
    return filePath;
  }
}
