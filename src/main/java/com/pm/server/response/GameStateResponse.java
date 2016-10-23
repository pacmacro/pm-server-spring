package com.pm.server.response;

import com.pm.server.datatype.GameState;

public class GameStateResponse {

	private GameState state;

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

}
