package com.pm.server.datatype;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoordinateImpl implements Coordinate {

	private double latitude;
	private double longitude;

	private final static Logger log =
			LogManager.getLogger(CoordinateImpl.class.getName());

	public CoordinateImpl(double latitude, double longitude) {

		log.debug(
				"Creating coordinate with latitude {} and longitude {}",
				latitude, longitude
		);

		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) {

		log.debug("Setting latitude to {}", latitude);

		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {

		log.debug("Setting longitude to {}", longitude);

		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

}
