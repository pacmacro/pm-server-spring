package com.pm.server.response;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;

/**
 * Contains all details of a single Player. 
 *
 */
public class PlayerDetailsResponse {

	private PlayerName name;

	private PlayerState state;

	private Coordinate location;

	public PlayerName getName() {
		return name;
	}

	public void setName(PlayerName playerName) {
		this.name = playerName;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

}
