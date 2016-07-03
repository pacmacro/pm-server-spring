package com.pm.server.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.player.Pacman;
import com.pm.server.player.PacmanImpl;

public class PacmanRepositoryTest extends TestTemplate {

	private List<Pacman> pacmanList = new ArrayList<Pacman>();

	private final static Integer numOfPacmans = 2;

	private static final List<Integer> randomIdList = Arrays.asList(
			95873,
			30839,
			34918
	);

	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
			new CoordinateImpl(45983.39872, 98347.39182),
			new CoordinateImpl(39487.22889, 90893.32281),
			new CoordinateImpl(49990.12929, 48982.39489)
	);

	@Autowired
	private PacmanRepository pacmanRepository;

	private static final Logger log =
			LogManager.getLogger(PacmanRepositoryTest.class.getName());

	@Before
	public void setUp() {

		for(Integer i = 0; i < numOfPacmans; i++) {

			// Arbitrary decimal values
			Coordinate coordinate = new CoordinateImpl(
					(i+12345.6789) / 100,
					(i-9876.54321) / 100
			);

			Pacman pacman = new PacmanImpl();
			pacman.setId(i);
			pacman.setLocation(coordinate);

			pacmanList.add(pacman);

		}
		assert(pacmanList.size() == numOfPacmans);

	}

	@After
	public void cleanUp() {
		
		pacmanRepository.clearPlayers();
		assert(pacmanRepository.getPlayer() == null);

		pacmanList.clear();
		assert(pacmanList.size() == 0);

	}

	@Test
	public void unitTest_addPlayer() {

		// Given
		Pacman pacman = pacmanList.get(0);

		// When
		addPlayer_failUponException(pacman);

		// Then
		Pacman pacmanReturned = pacmanRepository.getPlayer();
		assertEquals(pacman.getId(), pacmanReturned.getId());
		assertEquals(pacman.getLocation(), pacmanReturned.getLocation());

	}

	@Test
	public void unitTest_addPlayer_noId() {

		// Given
		Pacman pacman = pacmanList.get(0);
		pacman.setId(null);

		// When
		addPlayer_failUponException(pacman);

		// Then
		Pacman pacman_returned = pacmanRepository.getPlayer();
		assertEquals(pacman.getLocation(), pacman_returned.getLocation());

	}

	@Test
	public void unitTest_addPlayer_noLocation() {

		// Given
		Pacman pacman = pacmanList.get(0);
		pacman.setLocation(null);

		// When
		addPlayer_failUponException(pacman);

		// Then
		Pacman pacman_returned = pacmanRepository.getPlayer();
		assertEquals(pacman.getId(), pacman_returned.getId());

	}

	@Test
	public void unitTest_addPlayer_nullPlayer() {

		// Given

		// When
		addPlayer_failUponException(null);

		// Then
		assertNull(pacmanRepository.getPlayer());

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_addPlayer_pacmanAlreadyExists() throws Exception {

		// Given
		Pacman pacman1 = pacmanList.get(0);
		Pacman pacman2 = pacmanList.get(1);
		addPlayer_failUponException(pacman1);

		// When
		pacmanRepository.addPlayer(pacman2);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_deletePlayerById() {

		// Given
		addPlayer_failUponException(pacmanList.get(0));
		Integer randomId = randomIdList.get(0);

		// When
		try {
			pacmanRepository.deletePlayerById(randomId);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		assertNull(pacmanRepository.getPlayer());

	}

	@Test
	public void unitTest_deletePlayerById_noId() {

		// Given
		addPlayer_failUponException(pacmanList.get(0));

		// When
		try {
			pacmanRepository.deletePlayerById(null);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		assertNull(pacmanRepository.getPlayer());

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_deletePlayerById_noPlayer() throws Exception {

		// Given
		Integer randomId = randomIdList.get(0);

		// When
		pacmanRepository.deletePlayerById(randomId);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_clearPlayers() {

		// Given
		addPlayer_failUponException(pacmanList.get(0));

		// When
		pacmanRepository.clearPlayers();

		// Then
		assertNull(pacmanRepository.getPlayer());

	}

	@Test
	public void unitTest_clearPlayers_noPlayer() {

		// Given

		// When
		pacmanRepository.clearPlayers();

		// Then
		assertNull(pacmanRepository.getPlayer());

	}

	@Test
	public void unitTest_numOfPlayers_0players() {

		// Given

		// When
		Integer numOfPlayers = pacmanRepository.numOfPlayers();

		// Then
		assertEquals((Integer)0, numOfPlayers);

	}

	@Test
	public void unitTest_numofPlayers_1player() {

		// Given
		addPlayer_failUponException(pacmanList.get(0));

		// When
		Integer numOfPlayers = pacmanRepository.numOfPlayers();

		// Then
		assertEquals((Integer)1, numOfPlayers);

	}

	@Test
	public void unitTest_getPlayerById() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);
		Integer randomId = randomIdList.get(0);

		// When
		Pacman pacmanReturned = pacmanRepository.getPlayerById(randomId);

		// Then
		assertSame(pacman.getLocation(), pacmanReturned.getLocation());

	}

	@Test
	public void unitTest_getPlayerById_nullAddedId() {

		// Given
		Pacman pacman = pacmanList.get(0);
		pacman.setId(null);
		addPlayer_failUponException(pacman);

		Integer randomId = randomIdList.get(0);

		// When
		Pacman pacmanReturned = pacmanRepository.getPlayerById(randomId);

		// Then
		assertSame(pacman.getLocation(), pacmanReturned.getLocation());

	}

	@Test
	public void unitTest_getPlayerById_nullRequestedId() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);

		// When
		Pacman pacmanReturned = pacmanRepository.getPlayerById(null);

		// Then
		assertSame(pacman.getLocation(), pacmanReturned.getLocation());

	}

	@Test
	public void unitTest_getPlayerById_noPacman() {

		// Given
		Integer randomId = randomIdList.get(0);

		// When
		Pacman pacmanReturned = pacmanRepository.getPlayerById(randomId);

		// Then
		assertNull(pacmanReturned);

	}

	@Test
	public void unitTest_getPlayer() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);

		// When
		Pacman pacmanReturned = pacmanRepository.getPlayer();

		// Then
		assertEquals(pacman, pacmanReturned);

	}

	@Test
	public void unitTest_getPlayer_noPacman() {

		// Given

		// When
		Pacman pacmanReturned = pacmanRepository.getPlayer();

		// Then
		assertNull(pacmanReturned);

	}

	@Test
	public void unitTest_getAllPlayers() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);

		// When
		List<Pacman> pacmanReturnedList = pacmanRepository.getAllPlayers();

		// Then
		assertSame(pacman, pacmanReturnedList.get(0));

	}

	@Test
	public void unitTest_getAllPlayers_noPacman() {

		// Given

		// When
		List<Pacman> pacmanReturnedList = pacmanRepository.getAllPlayers();

		// Then
		assertTrue(pacmanReturnedList.isEmpty());

	}

	@Test
	public void unitTest_setPlayerLocationById() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);
		Coordinate newLocation = randomCoordinateList.get(0);

		// When
		pacmanRepository.setPlayerLocationById(pacman.getId(), newLocation);

		// Then
		Pacman pacmanReturned = pacmanRepository.getPlayer();
		assertSame(newLocation, pacmanReturned.getLocation());

	}

	@Test
	public void unitTest_setPlayerLocationById_randomId() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);

		Integer randomId = randomIdList.get(0);
		Coordinate newLocation = randomCoordinateList.get(0);

		// When
		pacmanRepository.setPlayerLocationById(randomId, newLocation);

		// Then
		Pacman pacmanReturned = pacmanRepository.getPlayer();
		assertSame(newLocation, pacmanReturned.getLocation());

	}

	@Test
	public void unitTest_setPlayerLocationById_nullId() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);
		Coordinate newLocation = randomCoordinateList.get(0);

		// When
		pacmanRepository.setPlayerLocationById(null, newLocation);

		// Then
		Pacman pacmanReturned = pacmanRepository.getPlayer();
		assertSame(newLocation, pacmanReturned.getLocation());

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_setPlayerLocationById_nullLocation() {

		// Given
		Integer randomId = randomIdList.get(0);

		// When
		pacmanRepository.setPlayerLocationById(randomId, null);

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setPlayerLocationById_noPacman()
			throws IllegalArgumentException {

		// Given
		Integer randomId = randomIdList.get(0);
		Coordinate newLocation = randomCoordinateList.get(0);

		// When
		pacmanRepository.setPlayerLocationById(randomId, newLocation);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_setPlayerLocation() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);
		Coordinate newLocation = randomCoordinateList.get(0);

		// When
		pacmanRepository.setPlayerLocation(newLocation);

		// Then
		Pacman pacmanReturned = pacmanRepository.getPlayer();
		assertSame(newLocation, pacmanReturned.getLocation());

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_setPlayerLocation_nullLocation() {

		// Given

		// When
		pacmanRepository.setPlayerLocation(null);

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setPlayerLocation_noPacman()
			throws IllegalArgumentException {

		// Given
		Coordinate newLocation = randomCoordinateList.get(0);

		// When
		pacmanRepository.setPlayerLocation(newLocation);

		// Then
		// Exception thrown above

	}

	private void addPlayer_failUponException(Pacman pacman) {
		try {
			pacmanRepository.addPlayer(pacman);
		}
		catch (Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

}