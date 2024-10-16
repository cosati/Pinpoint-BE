package com.cosati.photo_map.utils;

import java.io.IOException;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface FileHelper {
  FileHelper getDirectory(String folderPath) throws IOException;

  Path writeFile(MultipartFile file, String uniqueFileName) throws IOException;
}
