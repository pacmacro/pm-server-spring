package com.pm.server.controller;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.PacdotRegistry;

public class AdminControllerTest extends ControllerTestTemplate {

	@Autowired
	private PacdotRegistry pacdotRegistry;

//	private static final List<Coordinate> randomCoordinateList = Arrays.asList(
//			new CoordinateImpl(12345.54321, 95837.39821),
//			new CoordinateImpl(49381.30982, 39399.49932)
//	);

	private static final String BASE_MAPPING = "/admin";

//	private static final Logger log =
//			LogManager.getLogger(AdminControllerTest.class.getName());

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
	public void unitTest_resetPacdots() throws Exception {

		// Given
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			pacdot.setEaten(true);
		}

		final String path = pathForResetPacdots();

		// When
		mockMvc
				.perform(post(path))

		// Then
				.andExpect(status().isCreated());

		pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			assertFalse(pacdot.getEaten());
		}

	}

	private String pathForResetPacdots() {
		return BASE_MAPPING + "/pacdots/reset";
	}

}
