package com.pm.server.player;

import org.springframework.stereotype.Component;

import com.pm.server.datatype.Coordinate;

@Component
public class GhostImpl implements Ghost {

	private Integer id = 0;
	private Coordinate location;

	public void setId(Integer id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public Coordinate getLocation() {
		return location;
	}

}
