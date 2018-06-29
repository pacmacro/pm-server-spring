package com.pm.server.registry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.EatenDots;
import com.pm.server.datatype.Pacdot;
import com.pm.server.repository.PacdotRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

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
			pacdotRepository.addPacdot(new Pacdot(location, false, false));
		}

		locationList = readPacdotListFromFile(powerdotsFilename);
		for(Coordinate location : locationList) {
			pacdotRepository.addPacdot(new Pacdot(location, false, true));
		}

	}

	@Override
	public List<Pacdot> getInformationOfAllPacdots() {
		return Collections.unmodifiableList(pacdotRepository.getAllPacdots());
	}

	// TODO: Inefficient, but I am on an extremely tight schedule right now.
	// Even a counter variable would be better.
	@Override
	public boolean allPacdotsEaten() {
		List<Pacdot> pacdotList = pacdotRepository.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			if(!pacdot.isEaten()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public EatenDots eatPacdotsNearLocation(Coordinate location) {

		EatenDots eatenDotsReport = new EatenDots();

		/*
		 * Incredibly inefficient but I have no time to implement a quicker
		 * algorithm. If you feel like helping me improve this, please
		 * check out issue 52 on GitHub.
		 */
		List<Pacdot> pacdotList = getInformationOfAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			if(withinDistance(
					location, pacdot.getLocation(), pacdotCapturingDistance
				) && !pacdot.isEaten() ) {

				pacdot.setEaten();
				if(pacdot.isPowerdot()) {
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
