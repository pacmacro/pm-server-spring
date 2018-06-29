package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.EatenDots;
import com.pm.server.datatype.Pacdot;

public interface PacdotRegistry {

	/**
	 * Retrieves the information of all pacdots in the registry.
	 * 
	 * @return the information of all pacdots
	 */
	List<Pacdot> getInformationOfAllPacdots();

	/**
	 * Returns whether all Pacdots have been eaten.
	 * 
	 * @return Whether all Pacdots have been eaten
	 */
	boolean allPacdotsEaten();

	/**
	 * Sets all Pacdots within a set distance of the given location
	 * to eaten.
	 * 
	 * Idempotent (e.g. eating an eaten dot is valid)
	 * 
	 * @param location Location of the Player eating the dots
	 * @return A report on the number of Pacdots and Powerdots eaten
	 */
	EatenDots eatPacdotsNearLocation(Coordinate location);

	/**
	 * Resets all Pacdots to uneaten.
	 * 
	 * Idempotent (e.g. can be used when no Pacdots have been eaten yet)
	 * 
	 */
	void resetPacdots();

}
