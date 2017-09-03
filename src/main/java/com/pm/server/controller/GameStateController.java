package com.pm.server.controller;

import com.pm.server.manager.GameStateManager;
import com.pm.server.response.GameStateResponse;
import com.pm.server.response.ScoreResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gamestate")
public class GameStateController {

	private GameStateManager gameStateManager;

	private final static Logger log =
			LogManager.getLogger(GameStateController.class.getName());

	@Autowired
	public GameStateController(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}

	@RequestMapping(
			value="/score",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<ScoreResponse> getGameScore() {

		log.info("Mapped GET /gamestate/score");

		Integer score = gameStateManager.getScore();
		log.info("Retrieved score {}", score);

		ScoreResponse scoreResponse = new ScoreResponse();
		scoreResponse.setScore(score);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(scoreResponse);
	}

	@RequestMapping(
			value="",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<GameStateResponse> getGamestate() {
		log.info("Mapped GET /gamestate");

		GameStateResponse stateResponse = new GameStateResponse();
		stateResponse.setState(gameStateManager.getCurrentState());

		return ResponseEntity.status(HttpStatus.OK).body(stateResponse);
	}

}