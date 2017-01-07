package com.pm.server.controller;

import com.pm.server.ControllerTestTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MetricsControllerTest extends ControllerTestTemplate {

	private static final String BASE_MAPPING = "/metrics";

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
	public void unitTest_getMetrics() throws Exception {

		// Given

		final String path = BASE_MAPPING;

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk());

	}

}
