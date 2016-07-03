package com.pm.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.player.Ghost;
import com.pm.server.utils.JsonUtils;

@Repository
public class GhostRepositoryImpl implements GhostRepository {

	private List<Ghost> ghosts;

	private final static Integer MAX_GHOST_ID = 999;

	private final static Logger log =
			LogManager.getLogger(GhostRepositoryImpl.class.getName());

	GhostRepositoryImpl() {
		ghosts = new ArrayList<Ghost>();
	}

	@Override
	public Ghost getPlayerById(Integer id) {

		for(Ghost ghost : ghosts) {
			if(ghost.getId() == id) {
				return ghost;
			}
		}
		return null;

	}

	@Override
	public List<Ghost> getAllPlayers() {
		return ghosts;
	}

	@Override
	public void setPlayerLocationById(Integer id, Coordinate location) {

		if(id == null) {
			throw new NullPointerException("No id was given");
		}
		else if(location == null) {
			throw new NullPointerException("No location was given");
		}

		Ghost ghost = getPlayerById(id);
		if(ghost == null) {
			throw new IllegalArgumentException(
					"No ghost with the id " +
					Integer.toString(id) +
					" was found"
			);
		}

		String objectString = JsonUtils.objectToJson(location);
		log.debug(
				"Setting ghost with id {} to location {}",
				id, objectString
		);

		ghost.setLocation(location);
	}

	@Override
	public void addPlayer(Ghost ghost)
			throws NullPointerException, IllegalArgumentException {

		if(ghost == null) {
			throw new NullPointerException(
					"addPlayer() was given a null ghost."
			);
		}
		else if(ghost.getLocation() == null) {
			throw new NullPointerException(
					"addPlayer() was given a ghost with a null location."
			);
		}

		if(getPlayerById(ghost.getId()) == null) {

			String ghostString = JsonUtils.objectToJson(ghost);
			if(ghostString != null) {
				log.debug("Adding ghost {}", ghostString);
			}

			ghosts.add(ghost);
		}
		else {
			throw new IllegalArgumentException(
				"addGhost() was given an id belonging to a ghost which is " +
				"already in the registry."
			);
		}

	}

	@Override
	public void deletePlayerById(Integer id) throws Exception {

		for(Ghost ghost : ghosts) {
			if(ghost.getId() == id) {
				String ghostString = JsonUtils.objectToJson(ghost);
				log.debug("Removing ghost {}", ghostString);
				ghosts.remove(ghost);
				return;
			}
		}

		throw new IllegalArgumentException(
				"deleteGhostById() was given the id " +
				Integer.toString(id) +
				"which does not exist."
		);
	}

	@Override
	public void clearPlayers() {
		ghosts = new ArrayList<Ghost>();
	}

	@Override
	public Integer numOfPlayers() {
		return ghosts.size();
	}

	public Integer maxGhostId() {
		return MAX_GHOST_ID;
	}

}