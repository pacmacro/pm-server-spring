package com.pm.server.player;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;

@Repository
public interface GhostRepository {

	// Returns null if the ghost with the corresponding id is not found
	Ghost getGhostById(Integer id);

	List<Ghost> getAllGhosts();

	void setGhostLocationById(Integer id, Coordinate location);

	void addGhost(Ghost ghost) throws Exception;

	void deleteGhostById(Integer id) throws Exception;

	Integer numOfGhosts();

	Integer maxGhostId();

}
