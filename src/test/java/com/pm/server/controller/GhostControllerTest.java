package com.pm.server.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;
import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.CoordinateImpl;
public class GhostControllerTest extends ControllerTestTemplate {

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

	// Returns the ID of the created ghost
	private Integer createGhost_failUponException(Coordinate location) {

		String path =
				BASE_MAPPING + "/" +
				location.getLatitude() + "/" +
				location.getLongitude();
		String jsonContent = null;

		try {
			MvcResult result = mockMvc
					.perform(post(path))
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
