package com.pm.server.controller;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.pm.server.player.Ghost;
import com.pm.server.repository.GhostRepository;
import com.pm.server.utils.JsonUtils;
public class GhostControllerTest extends ControllerTestTemplate {

	@Autowired
	private GhostRepository ghostRepository;

	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
			new CoordinateImpl(12345.54321, 95837.39821),
			new CoordinateImpl(49381.30982, 39399.49932)
	);

	private static final String BASE_MAPPING = "/ghost";

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

		List<Ghost> ghostList = ghostRepository.getAllPlayers();

		List<Integer> ghostIdList = new ArrayList<Integer>();
		for(Ghost ghost : ghostList) {
			ghostIdList.add(ghost.getId());
		}

		for(Integer id : ghostIdList) {
			try {
				ghostRepository.deletePlayerById(id);
			}
			catch(Exception e) {
				log.error(e.getMessage());
				fail();
			}
		}

		assert(ghostRepository.numOfPlayers() == 0);

	}

	@Test
	public void unitTest_createGhost() throws Exception {
		// Given
		Coordinate location = randomCoordinateList.get(0);
		String body = JsonUtils.objectToJson(location);

		String path = pathForCreateGhost();

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
	public void unitTest_createGhost_sameLocation() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		String body = JsonUtils.objectToJson(location);

		String path = pathForCreateGhost();

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
	public void unitTest_createGhost_notANumber() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		String body =
				"{\"" +
				location.getLatitude() + "\":\"" +
				"longitude" +
				"\"}";

		String path = pathForCreateGhost();

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
	public void unitTest_deleteGhostById() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createGhost_failUponException(location);
		String path = pathForDeleteGhostById(id);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isOk());

	}

	@Test
	public void unitTest_deleteGhostById_noGhost() throws Exception {

		// Given
		Integer id = 2481;
		String path = pathForDeleteGhostById(id);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_deleteGhostById_incorrectId() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createGhost_failUponException(location);
		String path = pathForDeleteGhostById(id + 1);

		// When
		mockMvc
				.perform(delete(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getGhostLocationById() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createGhost_failUponException(location);
		String path = pathForGetGhostLocationById(id);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.location.latitude")
						.value(location.getLatitude())
				)
				.andExpect(jsonPath("$.location.longitude")
						.value(location.getLongitude())
				);

	}

	@Test
	public void unitTest_getGhostLocationById_wrongId() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createGhost_failUponException(location);
		String path = pathForGetGhostLocationById(id + 1);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getGhostLocationById_noGhost() throws Exception {

		// Given
		Integer id = 39482;
		String path = pathForGetGhostLocationById(id);

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isNotFound());

	}

	@Test
	public void unitTest_getAllGhostLocations_singleGhost() throws Exception {

		// Given
		Coordinate location = randomCoordinateList.get(0);
		Integer id = createGhost_failUponException(location);

		String path = pathForGetAllGhostLocations();

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
	public void unitTest_getAllGhostLocations_multipleGhosts() throws Exception {

		// Given
		Coordinate location0 = randomCoordinateList.get(0);
		Integer id0 = createGhost_failUponException(location0);

		Coordinate location1 = randomCoordinateList.get(1);
		Integer id1 = createGhost_failUponException(location1);

		String path = pathForGetAllGhostLocations();

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
	public void unitTest_getAllGhostLocations_noGhosts() throws Exception {

		// Given
		String path = pathForGetAllGhostLocations();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));

	}

	private String pathForCreateGhost() {
		return BASE_MAPPING;
	}

	private String pathForDeleteGhostById(Integer id) {
		return BASE_MAPPING + "/" + id;
	}

	private String pathForGetGhostLocationById(Integer id) {
		return BASE_MAPPING + "/" + id + "/" + "location";
	}

	private String pathForGetAllGhostLocations() {
		return BASE_MAPPING + "/" + "locations";
	}

	// Returns the ID of the created ghost
	private Integer createGhost_failUponException(Coordinate location) {

		String path = pathForCreateGhost();
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

}
