package com.pm.server.datatype;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pm.server.utils.JsonUtils;

public class Player {

	protected final Name name;
	protected Coordinate location = new Coordinate();
	protected PlayerState state = PlayerState.UNINITIALIZED;

	private final static Logger log =
			LogManager.getLogger(Player.class.getName());

	public Player(Name name) {
		if(name == null) {
			throw new NullPointerException("The Player must have a name.");
		}
		this.name = name;
	}

	public void resetLocation() {
		location.reset();
	}

	public Name getName() {
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

	public void setState(PlayerState state) throws NullPointerException {

		if(state == null) {
			String errorMessage = "setState() was given a null state.";
			log.warn(errorMessage);
			throw new NullPointerException(errorMessage);
		}
		else if(name != Name.Pacman && state == PlayerState.POWERUP) {
			String errorMessage = "A Ghost cannot be set to a POWERUP state.";
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		log.trace("Setting state to {}", state);
		this.state = state;

	}

	public PlayerState getState() {
		return state;
	}

	public enum Name {
		Pacman,
		Blinky,
		Inky,
		Pinky,
		Clyde
	}

}
