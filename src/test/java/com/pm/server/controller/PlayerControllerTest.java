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

import java.util.ArrayList;
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
import com.pm.server.player.Player;
import com.pm.server.repository.PlayerRepository;
import com.pm.server.utils.JsonUtils;
public class PlayerControllerTest extends ControllerTestTemplate {

	@Autowired
	private PlayerRepository playerRepository;

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

		List<Player> playerList = playerRepository.getAllPlayers();

		List<PlayerName> playerNameList = new ArrayList<PlayerName>();
		for(Player player : playerList) {
			playerNameList.add(player.getName());
		}

		for(PlayerName playerName : playerNameList) {
			try {
				playerRepository.deletePlayerByName(playerName);
			}
			catch(Exception e) {
				log.error(e.getMessage());
				fail();
			}
		}

		assert(playerRepository.numOfPlayers() == 0);

	}

	@Test
	public void unitTest_createPlayer() throws Exception {
		// Given
		Coordinate location = randomCoordinateList.get(0);
		String body = JsonUtils.objectToJson(location);

		String path = pathForCreatePlayer();

		// When
		mockMvc
				.perform(post(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());

	}

	@Test
	public void unitTest_createPlayer_sameLocation() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		String body = JsonUtils.objectToJson(location);

		String path = pathForCreatePlayer();

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
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());

	}

	@Test
	public void unitTest_createPlayer_notANumber() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		String body =
				"{\"" +
				location.getLatitude() + "\":\"" +
				"longitude" +
				"\"}";

		String path = pathForCreatePlayer();

		// When
		mockMvc
				.perform(post(path)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_createPlayer_noLocationGiven() throws Exception {

		// Given
		String path = pathForCreatePlayer();

		// When
		mockMvc
				.perform(post(path)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isBadRequest());

	}

	@Test
	public void unitTest_deletePlayerById() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);
		String path = pathForDeletePlayerById(id);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isOk());

	}

	@Test
	public void unitTest_deletePlayerById_noPlayer() throws Exception {

		// Given
		Integer id = 2481;
		String path = pathForDeletePlayerById(id);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_deletePlayerById_incorrectId() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);
		String path = pathForDeletePlayerById(id + 1);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getPlayerLocationById() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);
		String path = pathForGetPlayerLocationById(id);

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
	public void unitTest_getPlayerLocationById_wrongId() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);
		String path = pathForGetPlayerLocationById(id + 1);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getPlayerLocationById_noPlayer() throws Exception {

		// Given
		Integer id = 39482;
		String path = pathForGetPlayerLocationById(id);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getAllPlayerLocations_singlePlayer() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForGetAllPlayerLocations();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id")
						.value(id)
				)
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
		Coordinate location0 = randomCoordinateList.get(0);
		Integer id0 = createPlayer_failUponException(location0);

		Coordinate location1 = randomCoordinateList.get(1);
		Integer id1 = createPlayer_failUponException(location1);

		String path = pathForGetAllPlayerLocations();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id")
						.value(id0)
				)
				.andExpect(jsonPath("$[0].location.latitude")
						.value(location0.getLatitude())
				)
				.andExpect(jsonPath("$[0].location.longitude")
						.value(location0.getLongitude())
				)
				.andExpect(jsonPath("$[1].id")
						.value(id1)
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
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForGetPlayerStateById(id);

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
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForGetPlayerStateById(id + 1);

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
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForGetAllPlayerStates();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id")
						.value(id)
				)
				.andExpect(jsonPath("$[0].state").exists());

	}

	@Test
	public void unitTest_getAllPlayerStates_multiplePlayers() throws Exception {

		// Given
		Coordinate location0 = randomCoordinateList.get(0);
		Integer id0 = createPlayer_failUponException(location0);

		Coordinate location1 = randomCoordinateList.get(1);
		Integer id1 = createPlayer_failUponException(location1);

		String path = pathForGetAllPlayerStates();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(id0))
				.andExpect(jsonPath("$[0].state").exists())
				.andExpect(jsonPath("$[1].id").value(id1))
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
		Coordinate location_original = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location_original);

		String path = pathForSetPlayerLocationById(id);

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
				getPlayerLocationById_failUponException(id);
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

	}

	@Test
	public void unitTest_setPlayerStateById() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForSetPlayerStateById(id);

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
		PlayerState resultState = getPlayerStateById_failUponException(id);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerStateById_sameState() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForSetPlayerStateById(id);

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
		PlayerState resultState = getPlayerStateById_failUponException(id);
		assertEquals(updatedState, resultState);

	}

	@Test
	public void unitTest_setPlayerStateById_illegalPowerupState() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForSetPlayerStateById(id);

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
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForSetPlayerStateById(id);

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
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForSetPlayerStateById(id);

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
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createPlayer_failUponException(location);

		String path = pathForSetPlayerStateById(id + 1);

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

	private String pathForCreatePlayer() {
		return BASE_MAPPING;
	}

	private String pathForDeletePlayerById(Integer id) {
		return BASE_MAPPING + "/" + id;
	}

	private String pathForGetPlayerLocationById(Integer id) {
		return BASE_MAPPING + "/" + id + "/" + "location";
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

	// Returns the ID of the created player
	private Integer createPlayer_failUponException(Coordinate location) {

		String path = pathForCreatePlayer();
		String body = JsonUtils.objectToJson(location);
		String jsonContent = null;

		try {
			MvcResult result = mockMvc
					.perform(post(path)
							.content(body)
							.header("Content-Type", "application/json")
					)
					.andExpect(status().isOk())
					.andReturn();
			jsonContent = result.getResponse().getContentAsString();
		}
		catch(Exception e) {
			log.error(e.getMessage());
			fail();
		}

		assertNotNull(jsonContent);
		return JsonPath.read(jsonContent, "$.id");

	}

	private Coordinate getPlayerLocationById_failUponException(Integer id) {

		String path = pathForGetPlayerLocationById(id);
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
