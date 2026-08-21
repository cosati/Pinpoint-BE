package com.cosati.photo_map.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cosati.photo_map.domain.Picture;
import com.cosati.photo_map.domain.User;

public interface PictureRepository extends JpaRepository<Picture, UUID> {
  List<Picture> findByUser(User user);
}
