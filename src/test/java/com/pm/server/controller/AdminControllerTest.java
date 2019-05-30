package com.pm.server.controller;

import com.jayway.jsonpath.JsonPath;
import com.pm.server.ControllerTestTemplate;
import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.manager.AdminGameStateManager;
import com.pm.server.manager.PacdotManager;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.request.StateRequest;
import com.pm.server.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends ControllerTestTemplate {

	@Autowired
	private PacdotManager pacdotManager;

	@Autowired
	private PlayerRegistry playerRegistry;

	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
			new Coordinate(12345.54321, 95837.39821),
			new Coordinate(49381.30982, 39399.49932)
	);

	private static final String BASE_MAPPING = "/admin";

	private static final Logger log =
			LogManager.getLogger(AdminControllerTest.class.getName());

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext)
				.build();
	}

	@After
	public void tearDown() throws PmServerException {
		playerRegistry.reset();
	}

	@Test
	public void unitTest_resetPacdots() throws Exception {

		// Given
		final String path = pathForResetPacdots();

		// When
		mockMvc
				.perform(post(path))

		// Then
				.andExpect(status().isCreated());
	}

	@Test
	public void unitTest_setPlayerState() throws Exception {
		// Given
		Player.Name player = Player.Name.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerState(player);

		Player.State updatedState = Player.State.ACTIVE;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());

		// Then
		Player.State resultState = getPlayerState_failUponException(player);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerState_sameStateUninitialized()
			throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		String path = pathForSetPlayerState(player);

		Player.State updatedState = Player.State.UNINITIALIZED;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());

		// Then
		Player.State resultState = getPlayerState_failUponException(player);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerState_sameStateReady()
			throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerState(player);

		Player.State updatedState = Player.State.READY;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());

		// Then
		Player.State resultState = getPlayerState_failUponException(player);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerState_wrongName() throws Exception {

		// Given
		String path = BASE_MAPPING + "/player/PLAYER_NAME/state";

		Player.State updatedState = Player.State.UNINITIALIZED;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)

				// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_setPlayerState_noStateGiven()
			throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		String path = pathForSetPlayerState(player);

		// When
		mockMvc
				.perform(put(path)
						.contentType(MediaType.APPLICATION_JSON)
				)

				// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_setPlayerState_invalidStateValue() throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		String path = pathForSetPlayerState(player);

		String body = "{\"state\":\"invalidValue\"}";

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
	public void unitTest_setPlayerState_initialize()
			throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		String path = pathForSetPlayerState(player);

		Player.State updatedState = Player.State.READY;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

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
	public void unitTest_setPlayerState_uninitialize()
			throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerState(player);

		Player.State updatedState = Player.State.UNINITIALIZED;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

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
	public void unitTest_setPlayerState_ghostPowerupState() throws Exception {

		// Given
		Player.Name player = Player.Name.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerState(player);

		Player.State updatedState = Player.State.POWERUP;
		StateRequest updatedStateContainer =
				new StateRequest();
		updatedStateContainer.setState(updatedState.toString());
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
				)

				// Then
				.andExpect(status().isConflict());

	}

	private String pathForSelectPlayer(Player.Name player) {
		return "/player/" + player;
	}

	private String pathForGetPlayerState(Player.Name player) {
		return "/player/" + player + "/" + "state";
	}

	private String pathForResetPacdots() {
		return BASE_MAPPING + "/pacdots/reset";
	}

	private String pathForSetPlayerState(Player.Name player) {
		return BASE_MAPPING + "/player/" + player + "/" + "state";
	}

	private void selectPlayer_failUponException(
			Player.Name player, Coordinate location) {

		String path = pathForSelectPlayer(player);
		String body = JsonUtils.objectToJson(location);

		try {
			mockMvc
					.perform(post(path)
							.content(body)
							.contentType(MediaType.APPLICATION_JSON)
					)
					.andExpect(status().isOk());
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

	}

	private Player.State getPlayerState_failUponException(Player.Name player) {

		String path = pathForGetPlayerState(player);
		String jsonContent = null;

		try {
			MvcResult result = mockMvc
					.perform(get(path))
					.andExpect(status().isOk())
					.andReturn();
			jsonContent = result.getResponse().getContentAsString();
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		assertNotNull(jsonContent);
		String stateString = JsonPath.read(jsonContent, "$.state");

		Player.State state = null;
		try {
			state = Player.State.valueOf(stateString);
		}
		catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			fail();
		}

		assertNotNull(state);
		return state;

	}

}
