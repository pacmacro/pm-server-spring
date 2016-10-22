package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;

public interface PlayerRegistry {

	// Returns null if the player with the corresponding name is not found
	Player getPlayerByName(Player.Name name);

	List<Player> getAllPlayers();

	boolean allPlayersReady();

	void setPlayerLocationByName(Player.Name name, Coordinate location);

	void setPlayerStateByName(Player.Name name, Player.State state);

	void changePlayerStates(Player.State fromState, Player.State toState)
			throws NullPointerException;

	void reset();

	void resetHard() throws NullPointerException, IllegalArgumentException;

}
