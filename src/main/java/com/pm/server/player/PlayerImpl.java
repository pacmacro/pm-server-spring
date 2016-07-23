package com.pm.server.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;
import com.pm.server.utils.JsonUtils;

@Component
public abstract class PlayerImpl implements Player {

	protected PlayerName name;
	protected Integer id = 0;
	protected Coordinate location;
	protected PlayerState state = PlayerState.UNINITIALIZED;

	private final static Logger log =
			LogManager.getLogger(PlayerImpl.class.getName());

	PlayerImpl(PlayerName name) {
		if(name == null) {
			throw new NullPointerException("The Player must have a name.");
		}
		this.name = name;
	}

	public PlayerName getName() {
		return name;
	}

	public void setId(Integer id) {

		log.trace("Setting id to {}", id);

		this.id = id;
	}

	public int getId() {
		return id;
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

	public void setState(PlayerState state) throws NullPointerException {

		if(state == null) {
			String errorMessage = "setState() was given a null state.";
			log.warn(errorMessage);
			throw new NullPointerException(errorMessage);
		}

		log.trace("Setting location to {}", state);
		this.state = state;

	}

	public PlayerState getState() {
		return state;
	}

}
