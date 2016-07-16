package com.pm.server.repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Pacman;

public interface PacmanRepository extends PlayerRepository<Pacman> {

	Pacman getPlayer();

	void setPlayerLocation(Coordinate location);

	void setPlayerState(PlayerState state);

}
