package com.pm.server.repository;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;

public interface PlayerRepository <PlayerType> {

	void addPlayer(PlayerType player) throws Exception;

	void deletePlayerById(Integer id) throws Exception;

	// Returns null if the player with the corresponding id is not found
	PlayerType getPlayerById(Integer id);

	List<PlayerType> getAllPlayers();

	void setPlayerLocationById(Integer id, Coordinate location);

	void setPlayerStateById(Integer id, PlayerState state);

	void clearPlayers();

	Integer numOfPlayers();

}
