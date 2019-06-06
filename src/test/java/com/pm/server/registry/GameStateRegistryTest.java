package com.pm.server.registry;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.GameState;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GameStateRegistryTest extends TestTemplate {

	private GameStateRegistry gameStateRegistry;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	public GameStateRegistryTest() {
		gameStateRegistry = new GameStateRegistryImpl();
	}

	@After
	public void tearDown() {
		if (gameStateRegistry.getCurrentState() != GameState.INITIALIZING) {
			gameStateRegistry.resetGame();
		}
	}

	@Test
	public void unitTest_resetGame_validState() {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.resetGame();

		// Then
		// No error thrown

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_resetGame_sameState() {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.resetGame();

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_startGame_fromInitializing() {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.startGame();

		// Then
		// No error thrown

	}

	@Test
	public void unitTest_startGame_fromPaused() {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.pauseGame();
		assertEquals(
				GameState.PAUSED,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.startGame();

		// Then
		// No error thrown

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_startGame_sameState() {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.startGame();

		// Then
		// Exception thrown above

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_startGame_fromWinner() {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.setWinnerGhosts();
		assertEquals(
				GameState.FINISHED_GHOSTS_WIN,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.startGame();

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_pauseGame_validState() {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.pauseGame();

		// Then
		// No error thrown

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_pauseGame_fromInitializing() {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.pauseGame();

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_setWinnerPacman_validState() {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.setWinnerPacman();

		// Then
		// No error thrown

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_setWinnerPacman_fromInitializing() {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.setWinnerPacman();

		// Then
		// Exception thrown above

	}

	@Test
	public void unitTest_setWinnerGhosts_validState() {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.setWinnerGhosts();

		// Then
		// No error thrown

	}

	@Test(expected = IllegalStateException.class)
	public void unitTest_setWinnerGhosts_fromInitializing() {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		// When
		gameStateRegistry.setWinnerGhosts();

		// Then
		// Exception thrown above

	}

}
