package com.cosati.photo_map.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import com.cosati.photo_map.domain.Pin;

@RestResource
public interface PinRepository extends JpaRepository<Pin, Long> {
  Optional<Pin> findByFileName(String fileName);
}
