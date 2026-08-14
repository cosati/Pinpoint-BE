package com.cosati.photo_map.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cosati.photo_map.domain.FileData;

public interface FileDataRepository extends JpaRepository<FileData, UUID> {
  Optional<FileData> findByName(String fileName);
}
