package com.pm.server.player;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class GhostRepositoryImpl implements GhostRepository {

	List<Ghost> ghosts;

	public Ghost getGhostById(int id) {

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