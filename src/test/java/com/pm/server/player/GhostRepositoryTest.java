package com.pm.server.player;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.pm.server.TestTemplate;

public class GhostRepositoryTest extends TestTemplate {

	@Autowired
	private GhostRepository ghostRepository;

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

}
