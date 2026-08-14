package com.cosati.photo_map.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cosati.photo_map.domain.Pin;

public interface PinRepository extends JpaRepository<Pin, UUID> {
  Optional<Pin> findByFileName(String fileName);
}
