package com.pm.server.controller;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;
import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.datatype.PlayerStateContainer;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.utils.JsonUtils;
public class PlayerControllerTest extends ControllerTestTemplate {

	@Autowired
	private PlayerRegistry playerRegistry;

	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
			new CoordinateImpl(12345.54321, 95837.39821),
			new CoordinateImpl(49381.30982, 39399.49932)
	);

	private static final String BASE_MAPPING = "/player";

	private static final Logger log =
			LogManager.getLogger(PlayerControllerTest.class.getName());

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
	public void cleanUp() {

		try {
			playerRegistry.reset();
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

	}

	@Test
	public void unitTest_selectPlayer() throws Exception {

		// Given
		PlayerName playerName = PlayerName.Inky;
		final String path = pathForSelectPlayer(playerName);

		Coordinate location = randomCoordinateList.get(0);
		final String body = JsonUtils.objectToJson(location);

		// When
		mockMvc
				.perform(post(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

	}

	@Test
	public void unitTest_selectPlayer_sameLocation() throws Exception {

		// Given
		final String pathForInky = pathForSelectPlayer(PlayerName.Inky);
		final String pathForClyde = pathForSelectPlayer(PlayerName.Clyde);

		Coordinate location = randomCoordinateList.get(0);
		final String body = JsonUtils.objectToJson(location);

		mockMvc
				.perform(post(pathForInky)
						.content(body)
						.header("Content-Type", "application/json")
				)
				.andExpect(status().isOk());

		// When
		mockMvc
				.perform(post(pathForClyde)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

	}

	@Test
	public void unitTest_selectPlayer_reselect() throws Exception {

		// Given
		PlayerName playerName = PlayerName.Inky;
		final String path = pathForSelectPlayer(playerName);

		Coordinate location = randomCoordinateList.get(0);
		final String body = JsonUtils.objectToJson(location);

		mockMvc
				.perform(post(path)
						.content(body)
						.header("Content-Type", "application/json")
				)
				.andExpect(status().isOk());

		// When
		mockMvc
				.perform(post(path)
						.content(body)
						.header("Content-Type", "application/json")
		)

		// Then
				.andExpect(status().isConflict());

	}

	@Test
	public void unitTest_selectPlayer_noLocationGiven() throws Exception {

		// Given
		PlayerName playerName = PlayerName.Inky;
		final String path = pathForSelectPlayer(playerName);

		// When
		mockMvc
				.perform(post(path)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_deselectPlayer() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForDeselectPlayer(player);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isOk());

	}

	@Test
	public void unitTest_deselectPlayer_alreadyUninitialized() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		String path = pathForDeselectPlayer(player);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_deselectPlayer_wrongName() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = BASE_MAPPING + "/NAME_HERE";

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_getPlayerLocation() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForGetPlayerLocation(player);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.latitude")
						.value(location.getLatitude())
				)
				.andExpect(jsonPath("$.longitude")
						.value(location.getLongitude())
				);

	}

	@Test
	public void unitTest_getPlayerLocation_uninitialized() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		String path = pathForGetPlayerLocation(player);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.latitude")
						.value(0.0)
				)
				.andExpect(jsonPath("$.longitude")
						.value(0.0)
				);

	}

	@Test
	public void unitTest_getPlayerLocation_reUninitialized() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);
		deselectPlayer_failUponException(player);

		String path = pathForGetPlayerLocation(player);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.latitude")
						.value(0.0)
				)
				.andExpect(jsonPath("$.longitude")
						.value(0.0)
				);

	}

	@Test
	public void unitTest_getPlayerLocation_wrongName() throws Exception {

		// Given
		String path = BASE_MAPPING + "/player/PLAYER_NAME/location";

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getAllPlayerLocations_singlePlayer() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForGetAllPlayerLocations();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].location.latitude")
						.value(location.getLatitude())
				)
				.andExpect(jsonPath("$[0].location.longitude")
						.value(location.getLongitude())
				);

	}

	@Test
	public void unitTest_getAllPlayerLocations_multiplePlayers() throws Exception {

		// Given
		PlayerName player0 = PlayerName.Inky;
		Coordinate location0 = randomCoordinateList.get(0);
		selectPlayer_failUponException(player0, location0);

		PlayerName player1 = PlayerName.Pacman;
		Coordinate location1 = randomCoordinateList.get(1);
		selectPlayer_failUponException(player1, location1);

		String path = pathForGetAllPlayerLocations();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].location.latitude")
						.value(location0.getLatitude())
				)
				.andExpect(jsonPath("$[0].location.longitude")
						.value(location0.getLongitude())
				)
				.andExpect(jsonPath("$[1].location.latitude")
						.value(location1.getLatitude())
				)
				.andExpect(jsonPath("$[1].location.longitude")
						.value(location1.getLongitude())
				);

	}

	@Test
	public void unitTest_getAllPlayerLocations_noPlayers() throws Exception {

		// Given
		String path = pathForGetAllPlayerLocations();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

	}

	@Test
	public void unitTest_getPlayerStateById() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForGetPlayerStateById(212312);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state").exists());

	}

	@Test
	public void unitTest_getPlayerStateById_wrongId() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForGetPlayerStateById(123);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getPlayerStateById_noPlayer() throws Exception {

		// Given
		Integer randomId = 12931;

		String path = pathForGetPlayerStateById(randomId);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getAllPlayerStates_singlePlayer() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForGetAllPlayerStates();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
//				.andExpect(jsonPath("$[0].id")
//						.value(id)
//				)
				.andExpect(jsonPath("$[0].state").exists());

	}

	@Test
	public void unitTest_getAllPlayerStates_multiplePlayers() throws Exception {

		// Given
		PlayerName player0 = PlayerName.Inky;
		Coordinate location0 = randomCoordinateList.get(0);
		selectPlayer_failUponException(player0, location0);

		PlayerName player1 = PlayerName.Inky;
		Coordinate location1 = randomCoordinateList.get(1);
		selectPlayer_failUponException(player1, location1);

		String path = pathForGetAllPlayerStates();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
//				.andExpect(jsonPath("$[0].id").value(id0))
				.andExpect(jsonPath("$[0].state").exists())
//				.andExpect(jsonPath("$[1].id").value(id1))
				.andExpect(jsonPath("$[1].state").exists());

	}

	@Test
	public void unitTest_getAllPlayerStates_noPlayers() throws Exception {

		// Given
		String path = pathForGetAllPlayerStates();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

	}

	@Test
	public void unitTest_setPlayerLocationById() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location_original = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location_original);

		String path = pathForSetPlayerLocationById(123);

		Coordinate location_updated = randomCoordinateList.get(0);
		String body = JsonUtils.objectToJson(location_updated);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)
				.andExpect(status().isOk());

		// Then
		Coordinate location_result =
				getPlayerLocation_failUponException(player);
		assertEquals(location_updated.getLatitude(), location_result.getLatitude());
		assertEquals(location_updated.getLongitude(), location_result.getLongitude());

	}

	@Test
	public void unitTest_setPlayerLocationById_noPlayer() throws Exception {

		// Given
		Integer randomId = 29481;
		String pathForSetLocation = pathForSetPlayerLocationById(randomId);

		Coordinate location = randomCoordinateList.get(0);
		String body = JsonUtils.objectToJson(location);

		// When
		mockMvc
				.perform(put(pathForSetLocation)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_setPlayerLocationById_noLocationGiven()
			throws Exception {

		// Given
		Integer randomId = 29481;
		String pathForSetLocation = pathForSetPlayerLocationById(randomId);

		// When
		mockMvc
				.perform(put(pathForSetLocation)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isBadRequest());
		fail();

	}

	@Test
	public void unitTest_setPlayerStateById() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerStateById(123);

		PlayerState updatedState = PlayerState.READY;
		PlayerStateContainer updatedStateContainer =
				new PlayerStateContainer();
		updatedStateContainer.state = updatedState;
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)
				.andExpect(status().isOk());

		// Then
		PlayerState resultState = getPlayerStateById_failUponException(123);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerStateById_sameState() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerStateById(1231);

		PlayerState updatedState = PlayerState.READY;
		PlayerStateContainer updatedStateContainer =
				new PlayerStateContainer();
		updatedStateContainer.state = updatedState;
		String body = JsonUtils.objectToJson(updatedStateContainer);

		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)
				.andExpect(status().isOk());

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)
				.andExpect(status().isOk());

		// Then
		PlayerState resultState = getPlayerStateById_failUponException(123);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerStateById_illegalPowerupState() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerStateById(123);

		PlayerState updatedState = PlayerState.POWERUP;
		PlayerStateContainer updatedStateContainer =
				new PlayerStateContainer();
		updatedStateContainer.state = updatedState;
		String body = JsonUtils.objectToJson(updatedStateContainer);

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
	public void unitTest_setPlayerStateById_noStateGiven() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerStateById(123);

		// When
		mockMvc
				.perform(put(path)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_setPlayerStateById_invalidStateValue() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerStateById(123);

		String body = "{\"state\":\"invalidValue\"}";

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
	public void unitTest_setPlayerStateById_wrongId() throws Exception {

		// Given
		PlayerName player = PlayerName.Inky;
		Coordinate location = randomCoordinateList.get(0);
		selectPlayer_failUponException(player, location);

		String path = pathForSetPlayerStateById(123);

		PlayerState updatedState = PlayerState.READY;
		PlayerStateContainer updatedStateContainer =
				new PlayerStateContainer();
		updatedStateContainer.state = updatedState;
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_setPlayerStateById_noPlayer() throws Exception {

		// Given
		Integer randomId = 19349;
		String path = pathForSetPlayerStateById(randomId);

		PlayerState updatedState = PlayerState.READY;
		PlayerStateContainer updatedStateContainer =
				new PlayerStateContainer();
		updatedStateContainer.state = updatedState;
		String body = JsonUtils.objectToJson(updatedStateContainer);

		// When
		mockMvc
				.perform(put(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isNotFound());

	}

	private String pathForSelectPlayer(PlayerName player) {
		return BASE_MAPPING + "/" + player;
	}

	private String pathForDeselectPlayer(PlayerName player) {
		return BASE_MAPPING + "/" + player;
	}

	private String pathForGetPlayerLocation(PlayerName player) {
		return BASE_MAPPING + "/" + player + "/" + "location";
	}

	private String pathForGetAllPlayerLocations() {
		return BASE_MAPPING + "/" + "locations";
	}

	private String pathForGetPlayerStateById(Integer id) {
		return BASE_MAPPING + "/" + id + "/" + "state";
	}

	private String pathForGetAllPlayerStates() {
		return BASE_MAPPING + "/" + "states";
	}

	private String pathForSetPlayerLocationById(Integer id) {
		return BASE_MAPPING + "/" + id + "/" + "location";
	}

	private String pathForSetPlayerStateById(Integer id) {
		return BASE_MAPPING + "/" + id + "/" + "state";
	}

	private void selectPlayer_failUponException(
			PlayerName player, Coordinate location) {

		String path = pathForSelectPlayer(player);
		String body = JsonUtils.objectToJson(location);

		try {
			mockMvc
					.perform(post(path)
							.content(body)
							.header("Content-Type", "application/json")
					)
					.andExpect(status().isOk());
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

	}

	private void deselectPlayer_failUponException(
			PlayerName player) {

		String path = pathForSelectPlayer(player);

		try {
			mockMvc
					.perform(delete(path))
					.andExpect(status().isOk());
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

	}

	private Coordinate getPlayerLocation_failUponException(PlayerName player) {

		String path = pathForGetPlayerLocation(player);
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

		Double latitude = JsonPath.read(jsonContent, "$.latitude");
		assertNotNull(latitude);
		Double longitude = JsonPath.read(jsonContent, "$.longitude");
		assertNotNull(longitude);

		return new CoordinateImpl(latitude, longitude);

	}

	private PlayerState getPlayerStateById_failUponException(Integer id) {

		String path = pathForGetPlayerStateById(id);
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

		PlayerState state = null;
		try {
			state = PlayerState.valueOf(stateString);
		}
		catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			fail();
		}

		assertNotNull(state);
		return state;

	}

}
