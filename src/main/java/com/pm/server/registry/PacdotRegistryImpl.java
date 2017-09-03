package com.pm.server.registry;

import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import com.pm.server.datatype.EatenDots;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Pacdot;
import com.pm.server.repository.PacdotRepository;

@Repository
public class PacdotRegistryImpl implements PacdotRegistry {

	private PacdotRepository pacdotRepository;

	private String pacdotsFilename;

	private String powerdotsFilename;

	private Double pacdotCapturingDistance;

	private static final Logger log =
			LogManager.getLogger(PacdotRegistryImpl.class.getName());

	@Autowired
	public PacdotRegistryImpl(
			PacdotRepository pacdotRepository,
			@Value("${pacdots.locations.filename}") String pacdotsFilename,
			@Value("${powerdots.locations.filename}") String powerdotsFilename,
			@Value("${pacdot.capturing.distance}") Double pacdotCapturingDistance) {
		this.pacdotRepository = pacdotRepository;
		this.pacdotsFilename = pacdotsFilename;
		this.powerdotsFilename = powerdotsFilename;
		this.pacdotCapturingDistance = pacdotCapturingDistance;
	}

	/**
	 * 
	 * @throws Exception upon failure to read pacdot locations
	 * and instantiate all pacdots
	 * 
	 */
	@PostConstruct
	public void postConstruct() throws Exception {

		List<Coordinate> locationList;

		locationList = readPacdotListFromFile(pacdotsFilename);
		for(Coordinate location : locationList) {
			Pacdot pacdot = new Pacdot();
			pacdot.setLocation(location);
			pacdot.setEaten(false);
			pacdot.setPowerdot(false);
			pacdotRepository.addPacdot(pacdot);
		}

		locationList = readPacdotListFromFile(powerdotsFilename);
		for(Coordinate location : locationList) {
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

	// TODO: Inefficient, but I am on an extremely tight schedule right now.
	// Even a counter variable would be better.
	@Override
	public boolean allPacdotsEaten() {
		List<Pacdot> pacdotList = pacdotRepository.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			if(!pacdot.getEaten()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setEatenStatusByLocation(Coordinate location, boolean eaten)
			throws NullPointerException {
		pacdotRepository.setEatenStatusByLocation(location, eaten);
	}

	@Override
	public EatenDots eatPacdotsNearLocation(Coordinate location) {

		EatenDots eatenDotsReport = new EatenDots();

		/*
		 * Incredibly inefficient but I have no time to implement a quicker
		 * algorithm. If you feel like helping me improve this, please
		 * check out issue 52 on GitHub.
		 */
		List<Pacdot> pacdotList = getAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			if(withinDistance(
					location, pacdot.getLocation(), pacdotCapturingDistance
				) && !pacdot.getEaten() ) {

				pacdot.setEaten(true);
				if(pacdot.getPowerdot() == true) {
					eatenDotsReport.addEatenPowerdot();
				}
				else {
					eatenDotsReport.addEatenPacdot();
				}

			}
		}

		return eatenDotsReport;
	}

	@Override
	public void resetPacdots() {
		pacdotRepository.resetPacdots();
	}

	private List<Coordinate> readPacdotListFromFile(String filename)
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
					new TypeReference<List<Coordinate>>(){}
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
