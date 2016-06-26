package com.pm.server.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pm.server.datatype.Coordinate;
import com.pm.server.utils.JsonUtils;

public class PlayerResponse {

	private Integer id;

	private Coordinate location;

	private final static Logger log =
			LogManager.getLogger(PlayerResponse.class.getName());

	public void setId(Integer id) {

		log.debug("Setting id {}", id);

		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setLocation(Coordinate location) {

		String locationString = JsonUtils.objectToJson(location);
		if(locationString != null) {
			log.debug("Setting location to {}", locationString);
		}

		this.location = location;
	}

	public Coordinate getLocation() {
		return location;
	}

}
