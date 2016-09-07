package com.pm.server.datatype;

import com.pm.server.game.GameState;

public class GameStateContainer {

	private GameState state;

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

}
