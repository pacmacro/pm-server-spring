package com.pm.server.controller;

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
import com.pm.server.datatype.PlayerState;
import com.pm.server.datatype.PlayerStateContainer;
import com.pm.server.repository.PacmanRepository;
import com.pm.server.utils.JsonUtils;

public class PacmanControllerTest extends ControllerTestTemplate {

	@Autowired
	private PacmanRepository pacmanRepository;

	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
			new CoordinateImpl(12941.49901, 39231.05893),
			new CoordinateImpl(03912.08312, 11293.34921)
	);

	private static final String BASE_MAPPING = "/pacman";

	private static final Logger log =
			LogManager.getLogger(GhostControllerTest.class.getName());

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

		pacmanRepository.clearPlayers();
		assert(pacmanRepository.numOfPlayers() == 0);

	}

	@Test
	public void unitTest_createPacman() throws Exception {

		// Given
		final String path = pathForCreatePacman();

		Coordinate location = randomCoordinateList.get(0);
		final String body = JsonUtils.objectToJson(location);

		// When
		mockMvc
				.perform((post(path))
						.header("Content-Type", "application/json")
						.content(body)
				)

		// Then
				.andExpect(status().isOk());

	}

	@Test
	public void unitTest_deletePacman() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		createPacman_failUponException(location);

		final String pathForDeletePacman = pathForDeletePacman();
		final String pathForGetPacmanState = pathForGetPacmanState();

		// When
		mockMvc
				.perform(delete(pathForDeletePacman))

		// Then
				.andExpect(status().isOk());

		mockMvc
				.perform(get(pathForGetPacmanState))
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getPacmanLocation() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		createPacman_failUponException(location);

		final String path = pathForGetPacmanLocation();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.latitude")
						.value(location.getLatitude()))
				.andExpect(jsonPath("$.longitude")
						.value(location.getLongitude()));

	}

	@Test
	public void unitTest_getPacmanState() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		createPacman_failUponException(location);

		final String path = pathForGetPacmanState();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state").exists());

	}

	@Test
	public void unitTest_setPacmanLocation() throws Exception {

		// Given
		Coordinate location_original = randomCoordinateList.get(0);
		createPacman_failUponException(location_original);

		final String pathForSetPacmanLocation = pathForSetPacmanLocation();

		Coordinate location_new = randomCoordinateList.get(1);
		final String body = JsonUtils.objectToJson(location_new);

		// When
		mockMvc
				.perform(put(pathForSetPacmanLocation)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		Coordinate location_updated = getPacmanLocation_failUponException();
		assertEquals(
				location_new.getLatitude(),
				location_updated.getLatitude()
		);
		assertEquals(
				location_new.getLongitude(),
				location_updated.getLongitude()
		);

	}

	@Test
	public void unitTest_setPacmanState() throws Exception {

		// Given
		Coordinate location_original = randomCoordinateList.get(0);
		createPacman_failUponException(location_original);

		final String pathForSetPacmanState = pathForSetPacmanState();

		PlayerState state_new = PlayerState.ACTIVE;
		PlayerStateContainer stateContainer = new PlayerStateContainer();
		stateContainer.state = state_new;
		final String body = JsonUtils.objectToJson(stateContainer);

		// When
		mockMvc
				.perform(put(pathForSetPacmanState)
						.content(body)
						.header("Content-Type", "application/json")
				)

		// Then
				.andExpect(status().isOk());

		PlayerState state_updated = getPacmanState_failUponException();
		assertEquals(state_updated, state_new);

	}

	private static String pathForCreatePacman() {
		return BASE_MAPPING;
	}

	private static String pathForDeletePacman() {
		return BASE_MAPPING;
	}

	private static String pathForGetPacmanLocation() {
		return BASE_MAPPING + "/location";
	}

	private static String pathForGetPacmanState() {
		return BASE_MAPPING + "/state";
	}

	private static String pathForSetPacmanLocation() {
		return BASE_MAPPING + "/location";
	}

	private static String pathForSetPacmanState() {
		return BASE_MAPPING + "/state";
	}

	private void createPacman_failUponException(Coordinate location) {

		String path = pathForCreatePacman();
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

	private Coordinate getPacmanLocation_failUponException() {

		String path = pathForGetPacmanLocation();
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

	private PlayerState getPacmanState_failUponException() {

		String path = pathForGetPacmanState();
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