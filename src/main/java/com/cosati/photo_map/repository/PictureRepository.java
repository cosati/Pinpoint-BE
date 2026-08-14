package com.cosati.photo_map.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cosati.photo_map.domain.Picture;

public interface PictureRepository extends JpaRepository<Picture, UUID> {}
