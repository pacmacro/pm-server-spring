package com.pm.server.response;

import com.pm.server.datatype.Player;

public class PlayerNameAndPlayerStateResponse {

	public Player.Name name;

	public Player.State state;

	public PlayerNameAndPlayerStateResponse(
			Player.Name name, Player.State state) {
		this.name = name;
		this.state = state;
	}

}
