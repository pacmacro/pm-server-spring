package com.pm.server.response;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;

/**
 * Contains all details of a single Player. 
 *
 */
public class PlayerDetailsResponse {

	private Player.Name name;

	private Player.State state;

	private Coordinate location;

	public Player.Name getName() {
		return name;
	}

	public void setName(Player.Name playerName) {
		this.name = playerName;
	}

	public Player.State getState() {
		return state;
	}

	public void setState(Player.State state) {
		this.state = state;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

}
