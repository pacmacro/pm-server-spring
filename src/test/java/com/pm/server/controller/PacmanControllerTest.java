package com.pm.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;
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

	private static String pathForCreatePacman() {
		return BASE_MAPPING;
	}

}