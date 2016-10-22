package com.pm.server.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Pacdot;

public class PacdotRepositoryTest extends TestTemplate {

	private List<Pacdot> pacdotList = new ArrayList<Pacdot>();

	@Autowired
	private PacdotRepository pacdotRepository;

	private static final Logger log =
			LogManager.getLogger(PacdotRepositoryTest.class.getName());

	@Before
	public void setUp() {

		Pacdot pacdot1 = new Pacdot();
		pacdot1.setLocation(new Coordinate(123.456, 321.654));
		pacdot1.setEaten(false);
		pacdot1.setPowerdot(false);
		pacdotList.add(pacdot1);

		Pacdot pacdot2 = new Pacdot();
		pacdot2.setLocation(new Coordinate(390.412, 491.212));
		pacdot2.setEaten(true);
		pacdot2.setPowerdot(false);
		pacdotList.add(pacdot2);

		Pacdot pacdot3 = new Pacdot();
		pacdot3.setLocation(new Coordinate(221.156, 918.412));
		pacdot3.setEaten(false);
		pacdot3.setPowerdot(true);
		pacdotList.add(pacdot3);

		pacdotRepository.clear();
		assert(pacdotRepository.getAllPacdots().isEmpty());

	}

	@After
	public void cleanUp() {

		pacdotList.clear();

	}

	@Test
	public void unitTest_addPacdot() {

		// Given
		Pacdot pacdot = pacdotList.get(0);

		// When
		pacdotRepository.addPacdot(pacdot);

		// Then
		List<Pacdot> resultList = pacdotRepository.getAllPacdots();
		assertTrue(resultList.contains(pacdot));

	}

	@Test
	public void unitTest_addPacdot_multiple() {

		// Given

		// When
		for(Pacdot pacdot : pacdotList) {
			pacdotRepository.addPacdot(pacdot);
		}

		// Then
		List<Pacdot> resultList = pacdotRepository.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			assertTrue(resultList.contains(pacdot));	
		}

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_addPacdot_nullPacdot() throws Exception {

		// Given

		// When
		pacdotRepository.addPacdot(null);

		// Then
		// Exception thrown above

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_addPacdot_nullLocation() throws Exception {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		pacdot.setLocation(null);

		// When
		pacdotRepository.addPacdot(pacdot);

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_addPacdot_alreadyExists() throws Exception {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		pacdotRepository.addPacdot(pacdot);

		// When
		pacdotRepository.addPacdot(pacdot);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_deletePacdotByLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);

		// When
		pacdotRepository.deletePacdotByLocation(pacdot.getLocation());

		// Then
		List<Pacdot> resultList = pacdotRepository.getAllPacdots();
		assertFalse(resultList.contains(pacdot));

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_deletePacdotByLocation_noPacdot() throws Exception {

		// Given

		// When
		pacdotRepository.deletePacdotByLocation(
				pacdotList.get(0).getLocation()
		);

		// Then
		// Exception thrown above

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_deletePacdotByLocation_noLocation() throws Exception {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);

		// When
		pacdotRepository.deletePacdotByLocation(null);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_getPacdotByLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);

		// When
		Pacdot result = pacdotRepository.getPacdotByLocation(
				pacdot.getLocation()
		);

		// Then
		assertEquals(pacdot, result);

	}

	@Test
	public void unitTest_getPacdotByLocation_noPacdot() {

		// Given

		// When
		Pacdot result = pacdotRepository.getPacdotByLocation(
				pacdotList.get(0).getLocation()
		);

		// Then
		assertNull(result);

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_getPacdotByLocation_noLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);

		// When
		pacdotRepository.getPacdotByLocation(null);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_getAllPacdots() {

		// Given
		for(Pacdot pacdot : pacdotList) {
			pacdotRepository.addPacdot(pacdot);
		}

		// When
		List<Pacdot> resultList = pacdotRepository.getAllPacdots();

		// Then
		assertEquals(3, pacdotList.size());
		for(Pacdot pacdot : pacdotList) {
			assertTrue(resultList.contains(pacdot));
		}

	}

	@Test
	public void unitTest_getAllPacdots_noPacdot() {

		// Given

		// When
		List<Pacdot> resultList = pacdotRepository.getAllPacdots();

		// Then
		assertEquals(0, resultList.size());

	}

	@Test
	public void unitTest_setEatenStatusByLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);
		Coordinate location = pacdot.getLocation();
		Boolean oldEatenStatus = pacdot.getEaten();

		// When
		pacdotRepository.setEatenStatusByLocation(location, !oldEatenStatus);

		// Then
		Pacdot result = pacdotRepository.getPacdotByLocation(location);
		assertEquals(!oldEatenStatus, result.getEaten());

	}

	@Test
	public void unitTest_setEatenStatusByLocation_sameStatus() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);
		Coordinate location = pacdot.getLocation();
		Boolean oldEatenStatus = pacdot.getEaten();

		// When
		pacdotRepository.setEatenStatusByLocation(location, oldEatenStatus);

		// Then
		Pacdot result = pacdotRepository.getPacdotByLocation(location);
		assertEquals(oldEatenStatus, result.getEaten());

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setEatenStatusByLocation_noPacdot() {

		// Given
		Pacdot pacdot = pacdotList.get(0);

		// When
		pacdotRepository.setEatenStatusByLocation(
				pacdot.getLocation(), pacdot.getEaten()
		);

		// Then
		// Exception thrown above

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_setEatenStatusByLocation_nullLocation() {

		// Given
		Pacdot pacdot = pacdotList.get(0);
		addPacdot_failUponException(pacdot);
		Boolean oldEatenStatus = pacdot.getEaten();

		// When
		pacdotRepository.setEatenStatusByLocation(null, !oldEatenStatus);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_clear() {

		// Given
		pacdotRepository.addPacdot(pacdotList.get(0));

		// When
		pacdotRepository.clear();

		// Then
		assertTrue(pacdotRepository.getAllPacdots().isEmpty());

	}

	@Test
	public void unitTest_clear_multiplePacdots() {

		// Given
		for(Pacdot pacdot : pacdotList) {
			pacdotRepository.addPacdot(pacdot);
		}

		// When
		pacdotRepository.clear();

		// Then
		assertTrue(pacdotRepository.getAllPacdots().isEmpty());

	}

	private void addPacdot_failUponException(Pacdot pacdot) {
		try {
			pacdotRepository.addPacdot(pacdot);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

}
