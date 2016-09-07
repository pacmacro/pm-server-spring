package com.pm.server.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.game.GameState;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.request.StringStateContainer;
import com.pm.server.utils.JsonUtils;

public class AdminGameStateControllerTest extends ControllerTestTemplate {

	@Autowired
	private GameStateRegistry gameStateRegistry;

	private static final String BASE_MAPPING = "/admin/gamestate";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext)
				.build();

		if(gameStateRegistry.getCurrentState() != GameState.INITIALIZING) {
			gameStateRegistry.resetGame();
		}

	}

	@Test
	public void unitTest_getGameState() throws Exception {

		// Given
		final String path = pathForGetGameState();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state")
						.value(GameState.INITIALIZING.toString()));

	}

	@Test
	public void unitTest_putGameState_noState() throws Exception {

		// Given
		final String path = pathForPutGameState();

		// When
		mockMvc
				.perform(put(path)
				)

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_putGameState_invalidState() throws Exception {

		// Given

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState("Invalid state");
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_resetGame() throws Exception {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.INITIALIZING.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

	}

	@Test
	public void unitTest_resetGame_uninitialized() throws Exception {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.INITIALIZING.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_startGame() throws Exception {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

	}

	@Test
	public void unitTest_startGame_fromPaused() throws Exception {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.pauseGame();
		assertEquals(
				GameState.PAUSED,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

	}

	@Test
	public void unitTest_startGame_inProgress() throws Exception {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_startGame_winner() throws Exception {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.setWinnerGhosts();
		assertEquals(
				GameState.FINISHED_GHOSTS_WIN,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_pauseGame() throws Exception {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		assertEquals(
				GameState.PAUSED,
				gameStateRegistry.getCurrentState()
		);

	}

	@Test
	public void unitTest_pauseGame_initializing() throws Exception {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_pauseGame_paused() throws Exception {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.pauseGame();
		assertEquals(
				GameState.PAUSED,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_pauseGame_winner() throws Exception {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.setWinnerGhosts();		
		assertEquals(
				GameState.FINISHED_GHOSTS_WIN,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_setWinnerGhosts() throws Exception {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.FINISHED_GHOSTS_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		assertEquals(
				GameState.FINISHED_GHOSTS_WIN,
				gameStateRegistry.getCurrentState()
		);

	}

	@Test
	public void unitTest_setWinnerPacman() throws Exception {

		// Given
		gameStateRegistry.startGame();
		assertEquals(
				GameState.IN_PROGRESS,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.FINISHED_PACMAN_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		assertEquals(
				GameState.FINISHED_PACMAN_WIN,
				gameStateRegistry.getCurrentState()
		);

	}

	@Test
	public void unitTest_setWinner_uninitialized() throws Exception {

		// Given
		assertEquals(
				GameState.INITIALIZING,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.FINISHED_PACMAN_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_setWinner_paused() throws Exception {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.pauseGame();
		assertEquals(
				GameState.PAUSED,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.FINISHED_PACMAN_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_setWinner_currentWinner() throws Exception {

		// Given
		gameStateRegistry.startGame();
		gameStateRegistry.setWinnerGhosts();
		assertEquals(
				GameState.FINISHED_GHOSTS_WIN,
				gameStateRegistry.getCurrentState()
		);

		final String path = pathForPutGameState();

		StringStateContainer state = new StringStateContainer();
		state.setState(GameState.FINISHED_GHOSTS_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isConflict());

	}

	private String pathForGetGameState() {
		return BASE_MAPPING;
	}

	private String pathForPutGameState() {
		return BASE_MAPPING;
	}

}
