package com.pm.server.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;

@Component
public class GhostImpl extends PlayerImpl implements Ghost {

	private final static Logger log =
			LogManager.getLogger(GhostImpl.class.getName());

	public GhostImpl(PlayerName name) {
		super(name);
	}

	@Override
	public void setState(PlayerState state) throws IllegalArgumentException {

		if(state == PlayerState.POWERUP) {
			String errorMessage = "A Ghost cannot be set to a POWERUP state.";
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		super.setState(state);

	}

}