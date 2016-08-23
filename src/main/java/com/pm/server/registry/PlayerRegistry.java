package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Player;

public interface PlayerRegistry {

	// Returns null if the player with the corresponding name is not found
	Player getPlayerByName(PlayerName name);

	List<Player> getAllPlayers();

	void setPlayerLocationByName(PlayerName name, Coordinate location);

	void setPlayerStateByName(PlayerName name, PlayerState state);

	void setPlayersReadyToActive();

	void reset();

	void resetHard() throws NullPointerException, IllegalArgumentException;

}
