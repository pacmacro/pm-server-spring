package com.pm.server.response;

/**
 * Contains all properties of a single pacdot except for its
 * eaten/uneaten status.
 *
 */
public class PacdotUneatenResponse {

	private LocationResponse location;

	private Boolean powerdot;

	public LocationResponse getLocation() {
		return location;
	}

	public void setLocation(LocationResponse location) {
		this.location = location;
	}

	public Boolean getPowerdot() {
		return powerdot;
	}

	public void setPowerdot(Boolean powerdot) {
		this.powerdot = powerdot;
	}

}
