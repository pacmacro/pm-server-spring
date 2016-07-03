package com.pm.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.player.Pacman;
import com.pm.server.utils.JsonUtils;

@Repository
public class PacmanRepositoryImpl implements PacmanRepository {

	private Pacman pacman;

	private static final Logger log =
			LogManager.getLogger(PacmanRepositoryImpl.class.getClass());

	@Override
	public void addPlayer(Pacman player) throws Exception {

		String objectString = JsonUtils.objectToJson(player);
		if(objectString != null) {
			log.debug("Setting Pacman to {}", objectString);
		}

		if(pacman != null) {
			throw new IllegalStateException("A Pacman already exists.");
		}

		pacman = player;

	}

	@Override
	public void deletePlayerById(Integer id) throws Exception {

		if(pacman == null) {
			throw new IllegalStateException("No Pacman exists.");
		}

		clearPlayers();

	}
	@Override
	public Pacman getPlayerById(Integer id) {
		return getPlayer();
	}

	public Pacman getPlayer() {

		String objectString = JsonUtils.objectToJson(pacman);
		log.debug("Retrieving Pacman with properties {}", objectString);

		return pacman;
	}

	@Override
	public List<Pacman> getAllPlayers() {

		List<Pacman> pacmanList = new ArrayList<Pacman>();

		if(pacman == null) {
			log.debug("Retrieving Pacman: null");
			return pacmanList;
		}

		pacmanList.add(pacman);
		return pacmanList;

	}

	@Override
	public void setPlayerLocationById(Integer id, Coordinate location) {
		setPlayerLocation(location);
	}

	public void setPlayerLocation(Coordinate location) {

		if(location == null) {
			throw new NullPointerException("No location was given.");
		}
		else if(pacman == null) {
			throw new IllegalStateException("No pacman exists yet.");
		}

		String objectString = JsonUtils.objectToJson(location);
		if(objectString != null) {
			log.debug("Setting Pacman location to {}", objectString);
		}

		pacman.setLocation(location);

	}

	@Override
	public void clearPlayers() {

		String objectString = JsonUtils.objectToJson(pacman);
		if(objectString != null) {
			log.debug("Clearing Pacman (originally was {})", objectString);
		}

		pacman = null;
	}

	@Override
	public Integer numOfPlayers() {
		return (pacman == null) ? 0 : 1;
	}

}
