package com.pm.server.repository;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Pacdot;

public interface PacdotRepository {

	/**
	 * Adds a pacdot to the repository.
	 * 
	 * @param pacdot Pacdot to be added
	 * @throws IllegalArgumentException if a pacdot already exists at the
	 * given location
	 * @throws NullPointerException if the pacdot or the location is null
	 */
	void addPacdot(Pacdot pacdot)
			throws IllegalArgumentException, NullPointerException;

	/**
	 * Deletes a pacdot from the repository.
	 * 
	 * @param location Location of the pacdot
	 * @throws IllegalArgumentException if the pacdot does not exist in
	 * the repository
	 * @throws NullPointerException if the location is null
	 */
	void deletePacdotByLocation(Coordinate location)
			throws IllegalArgumentException, NullPointerException;

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
	 * Retrieves all pacdots in the repository.
	 * 
	 * @return all pacdots
	 */
	List<Pacdot> getAllPacdots();

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
	 * Resets all Pacdots to uneaten.
	 * 
	 * Idempotent (e.g. can be used when no Pacdots have been eaten yet)
	 * 
	 */
	void resetPacdots();

	/**
	 * Removes all pacdots from the repository.
	 */
	void clear();

}
