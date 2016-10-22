package com.pm.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.utils.JsonUtils;

@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

	private List<Player> playerList;

	private final static Logger log =
			LogManager.getLogger(PlayerRepositoryImpl.class.getName());

	public PlayerRepositoryImpl() {
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

		if(getPlayerByName(player.getName()) == null) {

			String objectString = JsonUtils.objectToJson(player);
			if(objectString != null) {
				log.debug("Adding player {}", objectString);
			}

			playerList.add(player);
		}
		else {
			throw new IllegalArgumentException(
				"addPlayer() was given a name belonging to a Player which is " +
				"already in the registry."
			);
		}

	}

	@Override
	public void deletePlayerByName(Player.Name name)
			throws IllegalArgumentException {

		for(Player player : playerList) {
			if(player.getName() == name) {
				String objectString = JsonUtils.objectToJson(player);
				log.debug("Removing player {}", objectString);
				playerList.remove(player);
				return;
			}
		}

		throw new IllegalArgumentException(
				"deleteplayerByName() was given the name " +
				name +
				"which does not exist."
		);
	}

	@Override
	public void clearPlayers() {
		playerList = new ArrayList<Player>();
	}

	@Override
	public Player getPlayerByName(Player.Name name) {

		for(Player player : playerList) {
			if(player.getName() == name) {
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
	public void setPlayerLocationByName(Player.Name name, Coordinate location) {

		if(name == null) {
			throw new NullPointerException("No name was given");
		}
		else if(location == null) {
			throw new NullPointerException("No location was given");
		}

		Player player = getPlayerByName(name);
		if(player == null) {
			throw new IllegalArgumentException(
					"No player with the name " +
					name +
					" was found"
			);
		}

		String oldLocationString = JsonUtils.objectToJson(player.getLocation());
		String newLocationString = JsonUtils.objectToJson(location);
		log.debug(
				"Setting player with name {} from location {} to location {}",
				name, oldLocationString, newLocationString
		);

		player.setLocation(location);
	}

	@Override
	public void setPlayerStateByName(Player.Name name, Player.State state) {

		if(name == null) {
			String errorMessage = "setplayerStateByName() was given a null name.";
			log.error(errorMessage);
			throw new NullPointerException(errorMessage);
		}
		Player player = getPlayerByName(name);
		if(player == null) {
			String errorMessage =
					"No player with the name " +
					name +
					" was found.";
			log.warn(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		log.debug(
				"Setting player with name {} from state {} to state {}",
				name, player.getState(), state
		);
		player.setState(state);

	}

	@Override
	public void changePlayerStates(Player.State fromState, Player.State toState)
			throws NullPointerException {
		if(fromState == null || toState == null) {
			throw new NullPointerException(
					"changePlayerStates() was given a null state."
			);
		}

		for(Player player : playerList) {
			if(player.getState() == fromState) {
				player.setState(toState);
			}
		}
	}

	@Override
	public Integer numOfPlayers() {
		return playerList.size();
	}

}