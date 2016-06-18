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
import com.pm.server.datatype.CoordinateImpl;

public class GhostRepositoryTest extends TestTemplate {

	@Autowired
	private GhostRepository ghostRepository;

	private static final double DELTA = 1e-15;

	private static final Logger log =
			LogManager.getLogger(GhostRepositoryTest.class.getName());

	@Before
	public void setUp() {

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
