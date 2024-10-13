package com.cosati.photo_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.cosati.photo_map.domain.Picture;

@RestResource
public interface PictureRepository extends JpaRepository<Picture, Long> {}
