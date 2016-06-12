package com.pm.server.player;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class GhostRepositoryImpl implements GhostRepository {

	private List<Ghost> ghosts;

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

		if(getGhostById(ghost.getId()) != null) {
			ghosts.add(ghost);
		}
		else {
			throw new IllegalArgumentException(
				"addGhost() was given an id belonging to a ghost which is " +
				"already in the registry."
			);
		}

	}

}