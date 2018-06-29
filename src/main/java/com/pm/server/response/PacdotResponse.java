package com.pm.server.response;

import com.pm.server.datatype.Pacdot;

/**
 * Contains all properties of a single pacdot.
 *
 */
public class PacdotResponse {

	private LocationResponse location;

	private Boolean eaten;

	private Boolean powerdot;

	public PacdotResponse() {
	}

	public PacdotResponse(Pacdot pacdot) {
		LocationResponse locationResponse = new LocationResponse();
		locationResponse.setLatitude(pacdot.getLocation().getLatitude());
		locationResponse.setLongitude(pacdot.getLocation().getLongitude());
		this.location = locationResponse;

		this.eaten = pacdot.isEaten();
		this.powerdot = pacdot.isPowerdot();
	}

	public LocationResponse getLocation() {
		return location;
	}

	public void setLocation(LocationResponse location) {
		this.location = location;
	}

	public Boolean getEaten() {
		return eaten;
	}

	public void setEaten(Boolean eaten) {
		this.eaten = eaten;
	}

	public Boolean getPowerdot() {
		return powerdot;
	}

	public void setPowerdot(Boolean powerdot) {
		this.powerdot = powerdot;
	}

}
