package com.pm.server.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.player.Pacman;
import com.pm.server.player.PacmanImpl;

public class PacmanRepositoryTest extends TestTemplate {

	private List<Pacman> pacmanList = new ArrayList<Pacman>();

	private final static Integer numOfPacmans = 2;

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
		Integer randomId = 12893;

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
		Integer randomId = 94720;

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
		Integer randomId = 12415;

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

		Integer randomId = 19482;

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
		Integer randomId = 83661;

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
		assertNull(pacmanReturnedList);

	}

	@Test
	public void unitTest_setPlayerLocationById() {

		// Given
		Pacman pacman = pacmanList.get(0);
		addPlayer_failUponException(pacman);
		Coordinate newLocation = new CoordinateImpl(4321.1234, 1234.4321);

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

		Integer randomId = 94827;
		Coordinate newLocation = new CoordinateImpl(1945.4918, 8490.3489);

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
		Coordinate newLocation = new CoordinateImpl(4738.2945, 9773.9457);

		// When
		pacmanRepository.setPlayerLocationById(null, newLocation);

		// Then
		Pacman pacmanReturned = pacmanRepository.getPlayer();
		assertSame(newLocation, pacmanReturned.getLocation());

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_setPlayerLocationById_nullLocation() {

		// Given
		Integer randomId = 97844;

		// When
		pacmanRepository.setPlayerLocationById(randomId, null);

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setPlayerLocationById_noPacman()
			throws IllegalArgumentException {

		// Given
		Integer randomId = 39884;
		Coordinate newLocation = new CoordinateImpl(2948.4938, 5938.1948);

		// When
		pacmanRepository.setPlayerLocationById(randomId, newLocation);

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