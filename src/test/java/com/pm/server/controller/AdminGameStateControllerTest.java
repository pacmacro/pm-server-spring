package com.pm.server.controller;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.GameState;
import com.pm.server.manager.AdminGameStateManager;
import com.pm.server.request.StateRequest;
import com.pm.server.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminGameStateControllerTest extends ControllerTestTemplate {

	@Mock
	private AdminGameStateManager adminGameStateManager;

	@InjectMocks
	private AdminGameStateController adminGameStateController;

	private static final String BASE_MAPPING = "/admin/gamestate";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext)
				.build();

	}

	@Test
	public void unitTest_changeGameState_validState() throws Exception {

		// Given
		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)

		// Then
		.andExpect(status().isOk());

	}

	@Test
	public void unitTest_changeGameState_invalidState() throws Exception {

		// Given
		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState("Invalid state");
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)

				// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_changeGameState_noBody() throws Exception {

		// Given
		final String path = pathForChangeGameState();

		// When
		mockMvc
				.perform(put(path))

				// Then
				.andExpect(status().isBadRequest());

	}

	/* // Integration tests, not unit tests

	@Test
	public void unitTest_changeGameState() throws Exception {

		// Given
		final String path = pathForChangeGameState();

		// When
		mockMvc
				.perform(put(path))

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_putGameState_invalidState() throws Exception {

		// Given

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState("Invalid state");
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.INITIALIZING.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.INITIALIZING.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.IN_PROGRESS.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.PAUSED.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.FINISHED_GHOSTS_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.FINISHED_PACMAN_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.FINISHED_PACMAN_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.FINISHED_PACMAN_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
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

		final String path = pathForChangeGameState();

		StateRequest state = new StateRequest();
		state.setState(GameState.FINISHED_GHOSTS_WIN.toString());
		final String body = JsonUtils.objectToJson(state);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)

		// Then
				.andExpect(status().isConflict());

	}

	*/

	private String pathForChangeGameState() {
		return BASE_MAPPING;
	}


}
