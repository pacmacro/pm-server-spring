package com.pm.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.registry.GameStateRegistry;
import com.pm.server.response.GameStateResponse;

@RestController
@RequestMapping("/gamestate")
public class GameStateController {

	@Autowired
	private GameStateRegistry gameStateRegistry;

	private final static Logger log =
			LogManager.getLogger(GameStateController.class.getName());

	@RequestMapping(
			value="",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<GameStateResponse> getGamestate(HttpServletResponse response) {
		log.info("Mapped GET /gamestate");

		GameStateResponse stateResponse = new GameStateResponse();
		stateResponse.setState(gameStateRegistry.getCurrentState());

		return ResponseEntity.status(HttpStatus.OK).body(stateResponse);
	}

}