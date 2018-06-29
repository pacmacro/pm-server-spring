package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.EatenDots;
import com.pm.server.datatype.Pacdot;

public interface PacdotRegistry {

	/**
	 * Retrieves the pacdot at the given location.
	 * 
	 * Returns null if no pacdot with the given location is found
	 * 
	 * @param location Location of the requested pacdot
	 * @throws NullPointerException if the location is null
	 * @return the requested pacdot
	 */
	Pacdot getPacdotByLocation(Coordinate location)
			throws NullPointerException;

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
	 * Sets the eaten status of a pacdot to true/false.
	 * 
	 * Idempotent (e.g. eaten to eaten is valid)
	 * 
	 * @param location Location of the requested pacdot
	 * @param eaten Whether or not the pacdot has been eaten
	 * @throws NullPointerException if the location given is null
	 */
	void setEatenStatusByLocation(Coordinate location, boolean eaten)
			throws NullPointerException;

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
