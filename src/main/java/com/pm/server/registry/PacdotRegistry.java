package com.pm.server.registry;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.EatenDotsReport;
import com.pm.server.datatype.Pacdot;

import java.util.List;

public interface PacdotRegistry {

	/**
	 * Retrieves the information of all pacdots in the registry.
	 * 
	 * @return the information of all pacdots
	 */
	List<Pacdot> getInformationOfAllPacdots();

	/**
	 * Retrieves the total number of Pacdots, eaten or uneaten.
	 *
	 * @return the total number of Pacdots, eaten or uneaten
	 */
	Integer getTotalCount();

	/**
	 * Retrieves the number of uneaten Pacdots.
	 *
	 * @return the number of uneaten Pacdots
	 */
	Integer getUneatenCount();

	/**
	 * Retrieves the number of uneaten Powerdots.
	 *
	 * @return the number of uneaten Powerdots
	 */
	Integer getUneatenPowerdotCount();

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
	EatenDotsReport eatPacdotsNearLocation(Coordinate location);

	/**
	 * Resets all Pacdots to uneaten.
	 * 
	 * Idempotent (e.g. can be used when no Pacdots have been eaten yet)
	 * 
	 */
	void resetPacdots();

}
