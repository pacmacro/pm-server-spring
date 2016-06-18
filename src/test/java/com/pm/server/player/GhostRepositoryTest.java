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
		Integer ghostId = 12345;
		Double latitude = 246.802;
		Double longitude = 135.791;

		Ghost ghost = new GhostImpl();
		ghost.setId(ghostId);
		ghost.setLocation(new CoordinateImpl(latitude, longitude));

		// When
		try {
			ghostRepository.addGhost(ghost);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		// Then
		Ghost ghostFromRepository = ghostRepository.getGhostById(ghostId);
		assertTrue(ghostFromRepository != null);

		assertEquals(ghost.getId(), ghostFromRepository.getId());
		assertEquals(
				ghost.getLocation().getLatitude(),
				ghostFromRepository.getLocation().getLatitude(),
				DELTA
		);
		assertEquals(
				ghost.getLocation().getLongitude(),
				ghostFromRepository.getLocation().getLongitude(),
				DELTA
		);

	}

}
