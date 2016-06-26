package com.pm.server.player;

import org.springframework.stereotype.Repository;

@Repository
public interface GhostRepository extends PlayerRepository<Ghost> {

	Integer maxGhostId();

}
