package com.pm.server.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pm.server.ControllerTestTemplate;

public class PacdotControllerTest extends ControllerTestTemplate {

//	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
//			new CoordinateImpl(12345.54321, 95837.39821),
//			new CoordinateImpl(49381.30982, 39399.49932)
//	);

	private static final String BASE_MAPPING = "/pacdots";

//	private static final Logger log =
//			LogManager.getLogger(PacdotControllerTest.class.getName());

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

	}

	@Test
	public void unitTest_getPacdotCount() throws Exception {

		// Given
		final String path = pathForGetPacdotCount();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.total").isNumber())
				.andExpect(jsonPath("$.eaten").isNumber())
				.andExpect(jsonPath("$.uneaten").isNumber())
				.andExpect(jsonPath("$.uneatenPowerdots").isNumber());

	}

	@Test
	public void unitTest_getUneatenPacdots() throws Exception {

		// Given
		final String path = pathForGetUneatenPacdots();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].location.latitude").isNumber())
				.andExpect(jsonPath("$[0].location.longitude").isNumber())
				.andExpect(jsonPath("$[0].powerdot").isBoolean());

	}

	@Test
	public void unitTest_getAllPacdots() throws Exception {

		// Given
		final String path = pathForGetAllPacdots();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].location.latitude").isNumber())
				.andExpect(jsonPath("$[0].location.longitude").isNumber())
				.andExpect(jsonPath("$[0].eaten").isBoolean())
				.andExpect(jsonPath("$[0].powerdot").isBoolean());

	}

	private String pathForGetPacdotCount() {
		return BASE_MAPPING + "/" + "count";
	}

	private String pathForGetUneatenPacdots() {
		return BASE_MAPPING + "/" + "uneaten";
	}

	private String pathForGetAllPacdots() {
		return BASE_MAPPING;
	}

}
