package com.pm.server.repository;

import static org.junit.Assert.assertEquals;
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

		// When
		addPlayer_failUponException(pacmanList.get(0));

		// Then
		Pacman pacman = pacmanRepository.getPlayer();
		assertEquals(pacmanList.get(0).getId(), pacman.getId());
		assertEquals(pacmanList.get(0).getLocation(), pacman.getLocation());

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