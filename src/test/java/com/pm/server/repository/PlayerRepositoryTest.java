package com.pm.server.repository;

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
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.player.Player;
import com.pm.server.player.PlayerImpl;

public class PlayerRepositoryTest extends TestTemplate {

	private Player player1;

	private Player player2;

	private Player player3;

	@Autowired
	private PlayerRepository playerRepository;

	private static final Logger log =
			LogManager.getLogger(PlayerRepositoryTest.class.getName());

	@Before
	public void setUp() {

		Integer player1_id = 12345;
		Coordinate player1_location = new CoordinateImpl(1.0, 0.1);
		player1 = new PlayerImpl(PlayerName.Blinky);
		player1.setId(player1_id);
		player1.setLocation(player1_location);

		Integer player2_id = 23456;
		Coordinate player2_location = new CoordinateImpl(2.0, 0.2);
		player2 = new PlayerImpl(PlayerName.Clyde);
		player2.setId(player2_id);
		player2.setLocation(player2_location);

		Integer player3_id = 34567;
		Coordinate player3_location = new CoordinateImpl(3.0, 0.3);
		player3 = new PlayerImpl(PlayerName.Pinky);
		player3.setId(player3_id);
		player3.setLocation(player3_location);

		playerRepository.clearPlayers();
		assert(playerRepository.getAllPlayers().isEmpty());

	}

	@Test
	public void unitTest_addPlayer() {

		// Given

		// When
		addPlayer_failUponException(player1);

		// Then
		Player playerFromRepository = playerRepository.getPlayerById(player1.getId());
		assertEquals(player1, playerFromRepository);

	}

	@Test
	public void unitTest_addPlayer_multiple() {

		// Given

		// When
		addPlayer_failUponException(player1);
		addPlayer_failUponException(player2);
		addPlayer_failUponException(player3);

		// Then
		Player player_retrieved;

		player_retrieved = playerRepository.getPlayerById(player1.getId());
		assertEquals(player1, player_retrieved);

		player_retrieved = playerRepository.getPlayerById(player2.getId());
		assertEquals(player2, player_retrieved);

		player_retrieved = playerRepository.getPlayerById(player3.getId());
		assertEquals(player3, player_retrieved);

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_addPlayer_nullPlayer() throws Exception {

		// Given

		// When
		playerRepository.addPlayer(null);

		// Then
		// Exception thrown above

	}

	@Test(expected = NullPointerException.class) 
	public void unitTest_addPlayer_nullPlayerLocation() throws Exception {

		// Given
		Player player = player1;
		player.setLocation(null);

		// When
		playerRepository.addPlayer(player);

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_addPlayer_conflictId() throws Exception {

		// Given
		addPlayer_failUponException(player1);

		// When
		playerRepository.addPlayer(player1);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_deletePlayerById() {

		// Given
		addPlayer_failUponException(player1);

		// When
		deletePlayerById_failUponException(player1.getId());

		// Then
		assertTrue(playerRepository.getPlayerById(player1.getId()) == null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_deletePlayerById_noPlayer() throws Exception {

		// Given

		// When
		playerRepository.deletePlayerById(player1.getId());

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_getPlayerById() {

		// Given
		addPlayer_failUponException(player1);

		// When
		Player player = playerRepository.getPlayerById(player1.getId());

		// Then
		assertEquals(player1, player);

	}

	@Test
	public void unitTest_getPlayerById_noPlayer() {

		// Given

		// When
		Player player = playerRepository.getPlayerById(player1.getId());

		// Then
		assertNull(player);

	}

	@Test
	public void unitTest_getAllPlayers() {

		// Given
		addPlayer_failUponException(player1);
		addPlayer_failUponException(player2);
		addPlayer_failUponException(player3);

		// When
		List<Player> playerList = playerRepository.getAllPlayers();

		// Then
		assertEquals(3, playerList.size());
		assertTrue(playerList.contains(player1));
		assertTrue(playerList.contains(player2));
		assertTrue(playerList.contains(player3));

	}

	@Test
	public void unitTest_getAllPlayers_noPlayer() {

		// Given

		// When
		List<Player> playerList = playerRepository.getAllPlayers();

		// Then
		assertEquals(0, playerList.size());

	}

	@Test
	public void unitTest_setPlayerLocationById() {

		// Given
		addPlayer_failUponException(player1);
		Coordinate oldPlayerLocation = player1.getLocation();
		Coordinate newPlayerLocation = new CoordinateImpl(9.8, 7.6);

		// When
		playerRepository.setPlayerLocationById(player1.getId(), newPlayerLocation);

		// Then
		Player player_newLocation = playerRepository.getPlayerById(player1.getId());

		assertFalse(oldPlayerLocation == player_newLocation.getLocation());
		assertTrue(newPlayerLocation == player_newLocation.getLocation());

	}

	@Test
	public void unitTest_setPlayerLocationById_sameId() {

		// Given
		addPlayer_failUponException(player1);

		// When
		playerRepository.setPlayerLocationById(
				player1.getId(),
				player1.getLocation()
		);

		// Then
		Player updatedPlayer = playerRepository.getPlayerById(player1.getId());

		assertEquals(updatedPlayer, player1);

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setPlayerLocationById_noPlayer() {

		// Given

		// When
		playerRepository.setPlayerLocationById(
				player1.getId(),
				player1.getLocation()
		);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_setPlayerStateById() {

		// Given
		Player player = player1;
		addPlayer_failUponException(player);
		PlayerState newState = PlayerState.ACTIVE;

		// When
		playerRepository.setPlayerStateById(player1.getId(), newState);

		// Then
		Player playerResult = playerRepository.getPlayerById(player.getId());
		assertEquals(newState, playerResult.getState());

	}

	@Test
	public void unitTest_setPlayerStateById_sameState() {

		// Given
		Player player = player1;
		addPlayer_failUponException(player);
		PlayerState state = PlayerState.CAPTURED;
		playerRepository.setPlayerStateById(player.getId(), state);

		// When
		playerRepository.setPlayerStateById(player.getId(), state);

		// Then
		Player playerResult = playerRepository.getPlayerById(player.getId());
		assertEquals(state, playerResult.getState());

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_setPlayerStateById_nullId() {

		// Given
		Player player = player1;
		addPlayer_failUponException(player);
		PlayerState newState = PlayerState.ACTIVE;

		// When
		playerRepository.setPlayerStateById(null, newState);

		// Then
		// Exception thrown above

	}

	@Test(expected = NullPointerException.class)
	public void unitTest_setPlayerStateById_nullState() {

		// Given
		Player player = player1;
		addPlayer_failUponException(player);

		// When
		playerRepository.setPlayerStateById(player.getId(), null);

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalArgumentException.class)
	public void unitTest_setPlayerStateById_powerUpState() {

		// GIven
		Player player = player1;
		addPlayer_failUponException(player);
		PlayerState illegalPlayerState = PlayerState.POWERUP;

		// When
		playerRepository.setPlayerStateById(player.getId(), illegalPlayerState);

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_numOfPlayers() {

		// Given
		assertEquals(Integer.valueOf(0), playerRepository.numOfPlayers());
		assert(playerRepository.getAllPlayers().isEmpty());

		// When
		addPlayer_failUponException(player1);

		// Then
		assertEquals(Integer.valueOf(1), playerRepository.numOfPlayers());

		// When
		deletePlayerById_failUponException(player1.getId());

		// Then
		assertEquals(Integer.valueOf(0), playerRepository.numOfPlayers());

	}

	private void addPlayer_failUponException(Player player) {
		try {
			playerRepository.addPlayer(player);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

	private void deletePlayerById_failUponException(Integer id) {
		try {
			playerRepository.deletePlayerById(id);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}
	}

}
