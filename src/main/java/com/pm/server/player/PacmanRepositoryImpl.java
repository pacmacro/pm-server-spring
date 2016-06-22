package com.pm.server.player;

import java.util.ArrayList;
import java.util.List;

import com.pm.server.datatype.Coordinate;

public class PacmanRepositoryImpl implements PacmanRepository {

	private Pacman pacman;

	@Override
	public Pacman getPlayerById(Integer id) {
		return getPlayer();
	}

	public Pacman getPlayer() {
		return pacman;
	}

	@Override
	public List<Pacman> getAllPlayers() {

		if(pacman == null) {
			return null;
		}

		List<Pacman> pacmanList = new ArrayList<Pacman>();
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
			throw new IllegalArgumentException("No pacman exists yet.");
		}

		pacman.setLocation(location);

	}

	@Override
	public void addPlayer(Pacman player) throws Exception {

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
	public void clearPlayers() {
		pacman = null;
	}

	@Override
	public Integer numOfPlayers() {
		return 1;
	}

}
