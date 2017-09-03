package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;

public interface PlayerRegistry {

	// Returns null if the player with the corresponding name is not found
	Coordinate getPlayerLocation(Player.Name name);

	// Returns null if the player with the corresponding name is not found
	Player.State getPlayerState(Player.Name name);

	void resetLocationOf(Player.Name name);

	void setPlayerLocationByName(Player.Name name, Coordinate location);

	void setPlayerStateByName(Player.Name name, Player.State state);

	void startFromReady();

	Integer getCapturedGhosts();

	/**
	 * Returns whether all ghosts have been captured.
	 *
	 * <p>
	 *     This is specifically defined as having at least one Ghost who
	 *     has been captured, and no Ghosts who are active.
	 * </p>
	 */
	Boolean allGhostsCaptured();

	void reset();

	void resetHard() throws NullPointerException, IllegalArgumentException;

}
