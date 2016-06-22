package com.pm.server.player;

import com.pm.server.datatype.Coordinate;

public interface PacmanRepository extends PlayerRepository<Pacman> {

	Pacman getPlayer();

	void setPlayerLocation(Coordinate location);

}
