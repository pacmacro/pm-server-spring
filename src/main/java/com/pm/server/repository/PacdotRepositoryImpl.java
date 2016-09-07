package com.pm.server.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Pacdot;
import com.pm.server.utils.JsonUtils;

@Repository
public class PacdotRepositoryImpl implements PacdotRepository {

	private List<Pacdot> pacdotList = new ArrayList<Pacdot>();

	private final static Logger log =
			LogManager.getLogger(PacdotRepositoryImpl.class.getName());

	@Override
	public void addPacdot(Pacdot pacdot)
			throws IllegalArgumentException, NullPointerException {

		if(pacdot == null) {
			final String errorMessage = "addPacdot() was given a null pacdot.";
			log.warn(errorMessage);
			throw new NullPointerException(errorMessage);
		}
		else if(pacdot.getLocation() == null) {
			final String errorMessage =
					"addPacdot() was given a pacdot with a null location.";
			log.warn(errorMessage);
			throw new NullPointerException(errorMessage);
		}

		if(getPacdotByLocation(pacdot.getLocation()) != null) {
			throw new IllegalArgumentException(
					"addPacdot() was given a location belonging to a Pacdot " +
					"which is already in the repository."
			);
		}

		String objectString = JsonUtils.objectToJson(pacdot);
		if(objectString != null) {
			log.debug("Adding pacdot {} to repository", objectString);
		}
		pacdotList.add(pacdot);
	}

	@Override
	public void deletePacdotByLocation(Coordinate location)
			throws IllegalArgumentException, NullPointerException {

		if(location == null) {
			throw new NullPointerException(
					"deletePacdotByLocation() was given a null location."
			);
		}

		for(Pacdot pacdot : pacdotList) {
			if(pacdot.getLocation() == location) {
				String objectString = JsonUtils.objectToJson(pacdot);
				log.debug("Removing pacdot {} from repository", objectString);
				pacdotList.remove(pacdot);
				return;
			}
		}

		throw new IllegalArgumentException(
				"deletePacdotByLocation() was given the location " +
				location +
				"which does not exist."
		);
	}

	@Override
	public Pacdot getPacdotByLocation(Coordinate location)
			throws NullPointerException {

		if(location == null) {
			throw new NullPointerException(
					"getPacdotByLocation() was given a null location."
			);
		}

		for(Pacdot pacdot : pacdotList) {
			if(pacdot.getLocation() == location) {
				log.trace(
						"Found pacdot {} in repository",
						JsonUtils.objectToJson(pacdot));
				return pacdot;
			}
		}
		return null;

	}

	@Override
	public List<Pacdot> getAllPacdots() {
		return pacdotList;
	}

	@Override
	public void setEatenStatusByLocation(Coordinate location, boolean eaten) {

		if(location == null) {
			final String errorMessage =
					"setEatenStatus() was given a null location.";
			log.warn(errorMessage);
			throw new NullPointerException(errorMessage);
		}

		Pacdot pacdot = getPacdotByLocation(location);
		if(pacdot == null) {
			throw new IllegalArgumentException(
					"setEatenStatus() could not find a pacdot at " +
					"the location " + location + ".");
		}

		String oldLocationString = JsonUtils.objectToJson(pacdot.getLocation());
		String newLocationString = JsonUtils.objectToJson(location);
		log.debug(
				"Setting pacdot with location {} from eaten status {} to {}",
				location, oldLocationString, newLocationString
		);

		pacdot.setEaten(eaten);
	}

	@Override
	public void resetPacdots() {
		for(Pacdot pacdot : pacdotList) {
			pacdot.setEaten(false);
		}
	}

	@Override
	public void clear() {
		pacdotList = new ArrayList<Pacdot>();
	}

}