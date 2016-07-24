package com.pm.server.repository;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Player;

public interface PlayerRepository {

	void addPlayer(Player player) throws Exception;

	void deletePlayerById(Integer id) throws Exception;

	// Returns null if the player with the corresponding id is not found
	Player getPlayerById(Integer id);

	List<Player> getAllPlayers();

	void setPlayerLocationById(Integer id, Coordinate location);

	void setPlayerStateById(Integer id, PlayerState state);

	void clearPlayers();

	Integer numOfPlayers();

	Integer maxPlayerId();

}
