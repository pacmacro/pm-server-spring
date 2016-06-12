package com.pm.server.response;

import com.pm.server.datatype.Coordinate;

public class GhostResponse {

	private Integer id;

	private Coordinate location;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public Coordinate getLocation() {
		return location;
	}

}
