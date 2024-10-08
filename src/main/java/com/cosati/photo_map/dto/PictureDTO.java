package com.cosati.photo_map.dto;

import java.util.Date;

public class PictureDTO {
	private long id;
	private String title;
	private String description;
	private Date dateTaken;
	private GeolocationDTO geolocation;
	private FileDataDTO fileData;
	
	public PictureDTO(
			long id, 
			String title, 
			String description, 
			Date dateTaken, 
			GeolocationDTO geolocation, 
			FileDataDTO fileData) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.dateTaken = dateTaken;
		this.geolocation = geolocation;
		this.fileData = fileData;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setName(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}

	public GeolocationDTO getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(GeolocationDTO geolocation) {
		this.geolocation = geolocation;
	}

	public FileDataDTO getFileData() {
		return fileData;
	}

	public void setFileData(FileDataDTO fileData) {
		this.fileData = fileData;
	}
}
