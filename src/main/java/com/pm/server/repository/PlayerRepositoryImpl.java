package com.pm.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Player;
import com.pm.server.utils.JsonUtils;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

	private List<Player> playerList;

	private final static Integer MAX_PLAYER_ID = 999;

	private final static Logger log =
			LogManager.getLogger(PlayerRepositoryImpl.class.getName());

	PlayerRepositoryImpl() {
		playerList = new ArrayList<Player>();
	}

	@Override
	public void addPlayer(Player player)
			throws NullPointerException, IllegalArgumentException {

		if(player == null) {
			throw new NullPointerException(
					"addPlayer() was given a null player."
			);
		}
		else if(player.getLocation() == null) {
			throw new NullPointerException(
					"addPlayer() was given a player with a null location."
			);
		}

		if(getPlayerById(player.getId()) == null) {

			String objectString = JsonUtils.objectToJson(player);
			if(objectString != null) {
				log.debug("Adding player {}", objectString);
			}

			playerList.add(player);
		}
		else {
			throw new IllegalArgumentException(
				"addPlayer() was given an id belonging to a Player which is " +
				"already in the registry."
			);
		}

	}

	@Override
	public void deletePlayerById(Integer id) throws Exception {

		for(Player player : playerList) {
			if(player.getId() == id) {
				String objectString = JsonUtils.objectToJson(player);
				log.debug("Removing player {}", objectString);
				playerList.remove(player);
				return;
			}
		}

		throw new IllegalArgumentException(
				"deleteplayerById() was given the id " +
				Integer.toString(id) +
				"which does not exist."
		);
	}

	@Override
	public void clearPlayers() {
		playerList = new ArrayList<Player>();
	}

	@Override
	public Player getPlayerById(Integer id) {

		for(Player player : playerList) {
			if(player.getId() == id) {
				log.trace("Found player {}", JsonUtils.objectToJson(player));
				return player;
			}
		}
		return null;

	}

	@Override
	public List<Player> getAllPlayers() {
		return playerList;
	}

	@Override
	public void setPlayerLocationById(Integer id, Coordinate location) {

		if(id == null) {
			throw new NullPointerException("No id was given");
		}
		else if(location == null) {
			throw new NullPointerException("No location was given");
		}

		Player player = getPlayerById(id);
		if(player == null) {
			throw new IllegalArgumentException(
					"No player with the id " +
					Integer.toString(id) +
					" was found"
			);
		}

		String oldLocationString = JsonUtils.objectToJson(player.getLocation());
		String newLocationString = JsonUtils.objectToJson(location);
		log.debug(
				"Setting player with id {} from location {} to location {}",
				id, oldLocationString, newLocationString
		);

		player.setLocation(location);
	}

	@Override
	public void setPlayerStateById(Integer id, PlayerState state) {

		if(id == null) {
			String errorMessage = "setplayerListtateById() was given a null id.";
			log.error(errorMessage);
			throw new NullPointerException(errorMessage);
		}
		Player player = getPlayerById(id);
		if(player == null) {
			String errorMessage =
					"No player with the id " +
					Integer.toString(id) +
					" was found.";
			log.warn(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		log.debug(
				"Setting player with id {} from state {} to state {}",
				id, player.getState(), state
		);
		player.setState(state);

	}

	@Override
	public Integer numOfPlayers() {
		return playerList.size();
	}

	@Override
	public Integer maxPlayerId() {
		return MAX_PLAYER_ID;
	}

}