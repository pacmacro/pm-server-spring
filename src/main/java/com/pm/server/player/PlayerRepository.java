package com.pm.server.player;

import java.util.List;

import com.pm.server.datatype.Coordinate;

public interface PlayerRepository <PlayerType> {

	// Returns null if the player with the corresponding id is not found
	PlayerType getPlayerById(Integer id);

	List<PlayerType> getAllPlayers();

	void setPlayerLocationById(Integer id, Coordinate location);

	void addPlayer(PlayerType player) throws Exception;

	void deletePlayerById(Integer id) throws Exception;

	void clearPlayers();

	Integer numOfPlayers();

}
