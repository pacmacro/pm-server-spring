package com.pm.server.repository;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Player;

public interface PlayerRepository {

	void addPlayer(Player player)
			throws NullPointerException, IllegalArgumentException;

	void deletePlayerByName(PlayerName name)
			throws IllegalArgumentException;

	// Returns null if the player with the corresponding name is not found
	Player getPlayerByName(PlayerName name);

	List<Player> getAllPlayers();

	void setPlayerLocationByName(PlayerName name, Coordinate location);

	void setPlayerStateByName(PlayerName name, PlayerState state);

	void changePlayerStates(PlayerState fromState, PlayerState toState)
			throws NullPointerException;

	void clearPlayers();

	Integer numOfPlayers();

}
