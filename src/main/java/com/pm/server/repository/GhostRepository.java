package com.pm.server.repository;

import org.springframework.stereotype.Repository;

import com.pm.server.player.Ghost;

@Repository
public interface GhostRepository extends PlayerRepository<Ghost> {

	Integer maxGhostId();

}
