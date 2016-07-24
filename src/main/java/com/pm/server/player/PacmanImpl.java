package com.pm.server.player;

import org.springframework.stereotype.Component;

import com.pm.server.datatype.PlayerName;

@Component
public class PacmanImpl extends PlayerImpl implements Pacman {

	public PacmanImpl(PlayerName name) {
		super(name);
		this.id = 0;
	}

}
