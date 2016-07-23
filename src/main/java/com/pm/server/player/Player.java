package com.pm.server.player;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;

public interface Player {

	PlayerName getName();

	void setId(Integer id);

	int getId();

	void setLocation(Coordinate location);

	Coordinate getLocation();

	void setState(PlayerState state);

	PlayerState getState();

}
