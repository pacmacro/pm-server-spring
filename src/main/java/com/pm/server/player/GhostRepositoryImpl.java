package com.pm.server.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;

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

	public Integer numOfGhosts() {
		return ghosts.size();
	}

	public Integer maxGhostId() {
		return MAX_GHOST_ID;
	}

}