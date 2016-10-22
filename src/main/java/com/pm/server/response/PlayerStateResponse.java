package com.pm.server.response;

import com.pm.server.datatype.Player;

public class PlayerStateResponse {

	private Player.State state;

	public void setState(Player.State state) {
		this.state = state;
	}

	public Player.State getState() {
		return this.state;
	}

}
