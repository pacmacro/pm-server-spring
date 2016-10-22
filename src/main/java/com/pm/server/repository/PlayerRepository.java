package com.pm.server.repository;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.datatype.PlayerState;

public interface PlayerRepository {

	void addPlayer(Player player)
			throws NullPointerException, IllegalArgumentException;

	void deletePlayerByName(Player.Name name)
			throws IllegalArgumentException;

	// Returns null if the player with the corresponding name is not found
	Player getPlayerByName(Player.Name name);

	List<Player> getAllPlayers();

	void setPlayerLocationByName(Player.Name name, Coordinate location);

	void setPlayerStateByName(Player.Name name, PlayerState state);

	void changePlayerStates(PlayerState fromState, PlayerState toState)
			throws NullPointerException;

	void clearPlayers();

	Integer numOfPlayers();

}
