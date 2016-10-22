package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.datatype.PlayerState;

public interface PlayerRegistry {

	// Returns null if the player with the corresponding name is not found
	Player getPlayerByName(Player.Name name);

	List<Player> getAllPlayers();

	boolean allPlayersReady();

	void setPlayerLocationByName(Player.Name name, Coordinate location);

	void setPlayerStateByName(Player.Name name, PlayerState state);

	void changePlayerStates(PlayerState fromState, PlayerState toState)
			throws NullPointerException;

	void reset();

	void resetHard() throws NullPointerException, IllegalArgumentException;

}
