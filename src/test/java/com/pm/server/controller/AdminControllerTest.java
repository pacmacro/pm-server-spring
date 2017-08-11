package com.pm.server.controller;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.Pacdot;
import com.pm.server.manager.AdminManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends ControllerTestTemplate {

	@Autowired
	private AdminManager adminManager;

	private static final String BASE_MAPPING = "/admin";

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
		final String path = pathForResetPacdots();

		// When
		mockMvc
				.perform(post(path))

		// Then
				.andExpect(status().isCreated());
	}

	private String pathForResetPacdots() {
		return BASE_MAPPING + "/pacdots/reset";
	}

}
