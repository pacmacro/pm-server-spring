package com.pm.server.response;

import com.pm.server.datatype.PlayerState;

public class PlayerStateResponse {

	private PlayerState state;

	public void setState(PlayerState state) {
		this.state = state;
	}

	public PlayerState getState() {
		return this.state;
	}

}
