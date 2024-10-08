package com.cosati.photo_map.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cosati.photo_map.domain.FileData;

@RestResource
public interface FileDataRepository extends JpaRepository<FileData, Long> {
	Optional<FileData> findByName(String fileName);
}
