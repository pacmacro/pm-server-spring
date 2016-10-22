package com.pm.server.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.utils.JsonUtils;

public class PlayerNameAndLocationResponse {

	private Player.Name name;

	private Coordinate location;

	private final static Logger log =
			LogManager.getLogger(PlayerNameAndLocationResponse.class.getName());

	public void setName(Player.Name name) {

		log.trace("Setting name to {}", name);

		this.name = name;
	}

	public Player.Name getName() {
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
