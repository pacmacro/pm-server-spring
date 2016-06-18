package com.pm.server.player;

import static org.junit.Assert.assertEquals;
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

	private static final double DELTA = 1e-15;

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

		List<Ghost> ghostList = ghostRepository.getAllGhosts();
		for(Ghost ghost : ghostList) {
			try {
				ghostRepository.deleteGhostById(ghost.getId());
			}
			catch(Exception e) {
				log.error(e.getMessage());
				fail();
			}
		}
		assertTrue(ghostRepository.getAllGhosts().isEmpty());

	}

	@Test
	public void unitTest_addGhost() {

		// Given

		// When
		try {
			ghostRepository.addGhost(ghost1);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		Ghost ghostFromRepository = ghostRepository.getGhostById(ghost1.getId());
		assertTrue(ghostFromRepository != null);

		assertEquals(ghostFromRepository.getId(), ghost1.getId());
		assertEquals(
				ghostFromRepository.getLocation().getLatitude(),
				ghost1.getLocation().getLatitude(),
				DELTA
		);
		assertEquals(
				ghostFromRepository.getLocation().getLongitude(),
				ghost1.getLocation().getLongitude(),
				DELTA
		);

	}

	@Test
	public void unitTest_deleteGhostById() {

		// Given
		try {
			ghostRepository.addGhost(ghost1);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// When
		try {
			ghostRepository.deleteGhostById(ghost1.getId());
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		assertTrue(ghostRepository.getGhostById(ghost1.getId()) == null);

	}

	@Test
	public void unitTest_numOfGhosts() {

		// Given
		assertEquals(Integer.valueOf(0), ghostRepository.numOfGhosts());
		assert(ghostRepository.getAllGhosts().isEmpty());

		// When
		try {
			ghostRepository.addGhost(ghost1);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		assertEquals(Integer.valueOf(1), ghostRepository.numOfGhosts());

		// When
		try {
			ghostRepository.deleteGhostById(ghost1.getId());
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		assertEquals(Integer.valueOf(0), ghostRepository.numOfGhosts());

	}


}
