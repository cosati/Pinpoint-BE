package com.cosati.photo_map.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class GeolocationDTO {
	private long id;
	private BigDecimal longitude;
	private BigDecimal latitude;
	
	public GeolocationDTO(long id, BigDecimal longitude, BigDecimal latitude) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeolocationDTO other = (GeolocationDTO) obj;
		return id == other.id && Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude);
	}
}
