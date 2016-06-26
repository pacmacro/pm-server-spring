package com.pm.server.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;

public class GhostRepositoryTest extends TestTemplate {

	private Ghost ghost1;

	private Ghost ghost2;

	private Ghost ghost3;

	@Autowired
	private GhostRepository ghostRepository;

	private static final Logger log =
			LogManager.getLogger(GhostRepositoryTest.class.getName());

	@Before
	public void setUp() {

		Integer ghost1_id = 12345;
		Coordinate ghost1_location = new CoordinateImpl(1.0, 0.1);
		ghost1 = new GhostImpl();
		ghost1.setId(ghost1_id);
		ghost1.setLocation(ghost1_location);

		Integer ghost2_id = 23456;
		Coordinate ghost2_location = new CoordinateImpl(2.0, 0.2);
		ghost2 = new GhostImpl();
		ghost2.setId(ghost2_id);
		ghost2.setLocation(ghost2_location);

		Integer ghost3_id = 34567;
		Coordinate ghost3_location = new CoordinateImpl(3.0, 0.3);
		ghost3 = new GhostImpl();
		ghost3.setId(ghost3_id);
		ghost3.setLocation(ghost3_location);

		ghostRepository.clearPlayers();
		assert(ghostRepository.getAllPlayers().isEmpty());

	}

	@Test
	public void unitTest_addGhost() {

		// Given

		// When
		addGhost_failUponException(ghost1);

		// Then
		Ghost ghostFromRepository = ghostRepository.getPlayerById(ghost1.getId());
		assertEquals(ghost1, ghostFromRepository);

	}

	@Test
	public void unitTest_addGhost_multiple() {

		// Given

		// When
		addGhost_failUponException(ghost1);
		addGhost_failUponException(ghost2);
		addGhost_failUponException(ghost3);

		// Then
		Ghost ghost_retrieved;

		ghost_retrieved = ghostRepository.getPlayerById(ghost1.getId());
		assertEquals(ghost1, ghost_retrieved);

		ghost_retrieved = ghostRepository.getPlayerById(ghost2.getId());
		assertEquals(ghost2, ghost_retrieved);

		ghost_retrieved = ghostRepository.getPlayerById(ghost3.getId());
		assertEquals(ghost3, ghost_retrieved);

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_addGhost_conflictId() throws Exception {

		// Given
		addGhost_failUponException(ghost1);

		// When
		ghostRepository.addPlayer(ghost1);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_deleteGhostById() {

		// Given
		addGhost_failUponException(ghost1);

		// When
		deleteGhostById_failUponException(ghost1.getId());

		// Then
		assertTrue(ghostRepository.getPlayerById(ghost1.getId()) == null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_deleteGhostById_noGhost() throws Exception {

		// Given

		// When
		ghostRepository.deletePlayerById(ghost1.getId());

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_getGhostById() {

		// Given
		addGhost_failUponException(ghost1);

		// When
		Ghost ghost = ghostRepository.getPlayerById(ghost1.getId());

		// Then
		assertEquals(ghost1, ghost);

	}

	@Test
	public void unitTest_getGhostById_noGhost() {

		// Given

		// When
		Ghost ghost = ghostRepository.getPlayerById(ghost1.getId());

		// Then
		assertNull(ghost);

	}

	@Test
	public void unitTest_getAllGhosts() {

		// Given
		addGhost_failUponException(ghost1);
		addGhost_failUponException(ghost2);
		addGhost_failUponException(ghost3);

		// When
		List<Ghost> ghostList = ghostRepository.getAllPlayers();

		// Then
		assertEquals(3, ghostList.size());
		assertTrue(ghostList.contains(ghost1));
		assertTrue(ghostList.contains(ghost2));
		assertTrue(ghostList.contains(ghost3));

	}

	@Test
	public void unitTest_getAllGhosts_noGhosts() {

		// Given

		// When
		List<Ghost> ghostList = ghostRepository.getAllPlayers();

		// Then
		assertEquals(0, ghostList.size());

	}

	@Test
	public void unitTest_setGhostLocationById() {

		// Given
		addGhost_failUponException(ghost1);
		Coordinate oldGhostLocation = ghost1.getLocation();
		Coordinate newGhostLocation = new CoordinateImpl(9.8, 7.6);

		// When
		ghostRepository.setPlayerLocationById(ghost1.getId(), newGhostLocation);

		// Then
		Ghost ghost_newLocation = ghostRepository.getPlayerById(ghost1.getId());

		assertFalse(oldGhostLocation == ghost_newLocation.getLocation());
		assertTrue(newGhostLocation == ghost_newLocation.getLocation());

	}

	@Test
	public void unitTest_setGhostLocationById_sameId() {

		// Given
		addGhost_failUponException(ghost1);

		// When
		ghostRepository.setPlayerLocationById(
				ghost1.getId(),
				ghost1.getLocation()
		);

		// Then
		Ghost updatedGhost = ghostRepository.getPlayerById(ghost1.getId());

		assertEquals(updatedGhost, ghost1);

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setGhostLocationById_noGhost() {

		// Given

		// When
		ghostRepository.setPlayerLocationById(
				ghost1.getId(),
				ghost1.getLocation()
		);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_numOfGhosts() {

		// Given
		assertEquals(Integer.valueOf(0), ghostRepository.numOfPlayers());
		assert(ghostRepository.getAllPlayers().isEmpty());

		// When
		addGhost_failUponException(ghost1);

		// Then
		assertEquals(Integer.valueOf(1), ghostRepository.numOfPlayers());

		// When
		deleteGhostById_failUponException(ghost1.getId());

		// Then
		assertEquals(Integer.valueOf(0), ghostRepository.numOfPlayers());

	}

	private void addGhost_failUponException(Ghost ghost) {
		try {
			ghostRepository.addPlayer(ghost);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

	private void deleteGhostById_failUponException(Integer id) {
		try {
			ghostRepository.deletePlayerById(id);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

}
