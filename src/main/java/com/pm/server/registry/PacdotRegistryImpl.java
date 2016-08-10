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

	private static List<CoordinateImpl> readPacdotListFromFile(String filename)
			throws Exception {

		InputStream inputStream = ClassLoader.
				getSystemResourceAsStream(filename);
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

}
