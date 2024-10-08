package com.cosati.photo_map.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cosati.photo_map.domain.Picture;
import com.cosati.photo_map.dto.PictureDTO;
import com.cosati.photo_map.repository.PictureRepository;
import com.cosati.photo_map.service.PictureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class PictureController {
	
	@Autowired
	private PictureRepository repository;
	
	@Autowired
	private PictureService pictureService;
	
	@GetMapping("/pictures")
	public ResponseEntity<List<PictureDTO>> getPictures() {
		return ResponseEntity.ok(
				repository.findAll()
					.stream()
					.map(picture -> pictureService.convertToDTO(picture))
					.collect(Collectors.toList()));
	}

	@GetMapping("/picture/{id}")
	public ResponseEntity<PictureDTO> getPictureById(@PathVariable("id") Long id) {
		Optional<Picture> picture = repository.findById(id);
		if (picture.isPresent()) {
			return ResponseEntity.ok(
					pictureService.convertToDTO(picture.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/addPicture")
	public ResponseEntity<PictureDTO> addPicture(
			@RequestParam("picture") String pictureJson,
			@RequestParam("file") MultipartFile file) {
		ObjectMapper objectMapper = new ObjectMapper();
		Picture picture;
		try {
			picture = objectMapper.readValue(pictureJson, Picture.class);
		} catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().build();
	    }
		return ResponseEntity.ok(
				pictureService.convertToDTO(
						pictureService.savePictureWithImage(picture, file)));
	}
	
	@DeleteMapping("/picture/{id}")
	public ResponseEntity<Void> deletePicture(@PathVariable("id") Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
