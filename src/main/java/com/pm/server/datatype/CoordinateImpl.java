package com.pm.server.datatype;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoordinateImpl implements Coordinate {

	private Double latitude;
	private Double longitude;

	private final static Logger log =
			LogManager.getLogger(CoordinateImpl.class.getName());

	public CoordinateImpl() {
		log.trace(
				"Creating coordinate with null latitude and longitude."
		);
	}

	public CoordinateImpl(Double latitude, Double longitude) {

		log.trace(
				"Creating coordinate with latitude {} and longitude {}",
				latitude, longitude
		);

		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object object) {

		if(object == null) {
			return false;
		}

		if(!CoordinateImpl.class.isAssignableFrom(object.getClass())) {
			return false;
		}
		final CoordinateImpl coordinateCompare = (CoordinateImpl) object;
		if(this.latitude != coordinateCompare.latitude) {
			return false;
		}
		else if(this.longitude != coordinateCompare.longitude) {
			return false;
		}

		return true;

	}

	public void setLatitude(Double latitude) {

		log.trace("Setting latitude to {}", latitude);

		this.latitude = latitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLongitude(Double longitude) {

		log.trace("Setting longitude to {}", longitude);

		this.longitude = longitude;
	}

	public Double getLongitude() {
		return longitude;
	}

}
