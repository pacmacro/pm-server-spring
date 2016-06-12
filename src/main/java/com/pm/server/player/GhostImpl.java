package com.pm.server.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pm.server.datatype.Coordinate;
import com.pm.server.utils.JsonUtils;

@Component
public class GhostImpl implements Ghost {

	private Integer id = 0;
	private Coordinate location;

	private final static Logger log =
			LogManager.getLogger(GhostImpl.class.getName());

	public void setId(Integer id) {

		log.debug("Setting id to {}", id);

		this.id = id;
	}

	public int getId() {
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
