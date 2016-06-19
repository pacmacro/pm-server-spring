package com.pm.server.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
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

	public Ghost getGhostById(Integer id) {

		for(Ghost ghost : ghosts) {
			if(ghost.getId() == id) {
				return ghost;
			}
		}
		return null;

	}

	public List<Ghost> getAllGhosts() {
		return ghosts;
	}

	public void setGhostLocationById(Integer id, Coordinate location) {

		if(id == null) {
			throw new NullPointerException("No id was given");
		}
		else if(location == null) {
			throw new NullPointerException("No location was given");
		}

		Ghost ghost = getGhostById(id);
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

	public void addGhost(Ghost ghost) throws Exception {

		if(getGhostById(ghost.getId()) == null) {

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

	public void deleteGhostById(Integer id) throws Exception {

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

	public void clearGhosts() {
		ghosts = new ArrayList<Ghost>();
	}

	public Integer numOfGhosts() {
		return ghosts.size();
	}

	public Integer maxGhostId() {
		return MAX_GHOST_ID;
	}

}