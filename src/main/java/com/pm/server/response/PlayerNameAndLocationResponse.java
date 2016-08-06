package com.pm.server.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.utils.JsonUtils;

public class PlayerNameAndLocationResponse {

	private PlayerName name;

	private Coordinate location;

	private final static Logger log =
			LogManager.getLogger(PlayerNameAndLocationResponse.class.getName());

	public void setName(PlayerName name) {

		log.trace("Setting name to {}", name);

		this.name = name;
	}

	public PlayerName getName() {
		return name;
	}

	public void setLocation(Coordinate location) {

		String locationString = JsonUtils.objectToJson(location);
		if(locationString != null) {
			log.trace("Setting location to {}", locationString);
		}

		this.location = location;
	}

	public Coordinate getLocation() {
		return location;
	}

}
