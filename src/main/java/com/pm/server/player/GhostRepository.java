package com.pm.server.player;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GhostRepository {

	// Returns null if the ghost with the corresponding id is not found
	Ghost getGhostById(Integer id);

	List<Ghost> getAllGhosts();

	void addGhost(Ghost ghost) throws Exception;

	Integer numOfGhosts();

	Integer maxGhostId();

}
