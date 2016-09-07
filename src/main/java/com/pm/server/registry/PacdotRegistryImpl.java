package com.pm.server.registry;

import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.datatype.Pacdot;
import com.pm.server.repository.PacdotRepository;

@Repository
public class PacdotRegistryImpl implements PacdotRegistry {

	@Autowired
	private PacdotRepository pacdotRepository;

	@Value("${pacdots.locations.filename}")
	private String pacdotsFilename;

	@Value("${powerdots.locations.filename}")
	private String powerdotsFilename;

	/**
	 * Distance in GPS coordinate units
	 */
	private static final Double DISTANCE_TO_PACDOTS = 0.0005;

	private final static Logger log =
			LogManager.getLogger(PacdotRegistryImpl.class.getName());

	/**
	 * 
	 * @throws Exception upon failure to read pacdot locations
	 * and instantiate all pacdots
	 * 
	 */
	@PostConstruct
	public void postConstruct() throws Exception {

		List<CoordinateImpl> locationList;

		locationList = readPacdotListFromFile(pacdotsFilename);
		for(CoordinateImpl location : locationList) {
			Pacdot pacdot = new Pacdot();
			pacdot.setLocation(location);
			pacdot.setEaten(false);
			pacdot.setPowerdot(false);
			pacdotRepository.addPacdot(pacdot);
		}

		locationList = readPacdotListFromFile(powerdotsFilename);
		for(CoordinateImpl location : locationList) {
			Pacdot pacdot = new Pacdot();
			pacdot.setLocation(location);
			pacdot.setEaten(false);
			pacdot.setPowerdot(true);
			pacdotRepository.addPacdot(pacdot);
		}

	}

	@Override
	public Pacdot getPacdotByLocation(Coordinate location)
			throws NullPointerException {
		return pacdotRepository.getPacdotByLocation(location);
	}

	@Override
	public List<Pacdot> getAllPacdots() {
		return pacdotRepository.getAllPacdots();
	}

	@Override
	public void setEatenStatusByLocation(Coordinate location, boolean eaten)
			throws NullPointerException {
		pacdotRepository.setEatenStatusByLocation(location, eaten);
	}

	@Override
	public Boolean eatPacdotsNearLocation(Coordinate location) {

		Boolean powerDotEaten = false;

		/*
		 * Incredibly inefficient but I have no time to implement a quicker
		 * algorithm. If you feel like helping me improve this, please
		 * check out issue 52 on GitHub.
		 */
		List<Pacdot> pacdotList = getAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			if(withinDistance(
					location,
					pacdot.getLocation(),
					DISTANCE_TO_PACDOTS)) {
				pacdot.setEaten(true);
				if(pacdot.getPowerdot() == true) {
					powerDotEaten = true;
				}
			}

		}

		return powerDotEaten;
	}

	@Override
	public void resetPacdots() {
		pacdotRepository.resetPacdots();
	}

	private List<CoordinateImpl> readPacdotListFromFile(String filename)
			throws Exception {

		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
		if(inputStream == null) {
			throw new IllegalArgumentException(
					"InputStream could not be opened for reading " +
					"file " + filename + ".");
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(
					inputStream,
					new TypeReference<List<CoordinateImpl>>(){}
			);
		} catch (Exception e) {
			log.fatal("Failed to read pacdot locations from file.");
			throw e;
		}

	}

	private Boolean withinDistance(
			Coordinate location1, Coordinate location2,
			Double distance) {
		Double latitudeDistance =
				Math.abs(location1.getLatitude() - location2.getLatitude());
		Double longitudeDistance =
				Math.abs(location1.getLongitude() - location2.getLongitude());

		return (square(latitudeDistance) + square(longitudeDistance))
				< square(distance);
	}

	private Double square(Double val) {
		return Math.pow(val, 2);
	}

}
