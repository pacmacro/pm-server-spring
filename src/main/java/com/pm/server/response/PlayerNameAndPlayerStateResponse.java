package com.pm.server.response;

import com.pm.server.datatype.Player;

public class PlayerNameAndPlayerStateResponse {

	private Player.Name name;

	private Player.State state;

	public PlayerNameAndPlayerStateResponse(
			Player.Name name, Player.State state) {
		this.name = name;
		this.state = state;
	}

	public Player.Name getName() {
		return name;
	}

	public void setName(Player.Name name) {
		this.name = name;
	}

	public Player.State getState() {
		return state;
	}

	public void setState(Player.State state) {
		this.state = state;
	}
}
