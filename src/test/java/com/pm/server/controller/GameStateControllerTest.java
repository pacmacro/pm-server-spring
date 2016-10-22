package com.pm.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pm.server.ControllerTestTemplate;
import com.pm.server.datatype.GameState;
import com.pm.server.registry.GameStateRegistry;

public class GameStateControllerTest extends ControllerTestTemplate {

	@Autowired
	private GameStateRegistry gameStateRegistry;

	private static final String BASE_MAPPING = "/gamestate";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		mockMvc = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext)
				.build();

		if(gameStateRegistry.getCurrentState() != GameState.INITIALIZING) {
			gameStateRegistry.resetGame();
		}

	}

	@Test
	public void unitTest_getGameState() throws Exception {

		// Given
		final String path = pathForGetGameState();

		// When
		mockMvc
				.perform(get(path))

		// Then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state")
						.value(GameState.INITIALIZING.toString()));

	}

	private String pathForGetGameState() {
		return BASE_MAPPING;
	}

}
