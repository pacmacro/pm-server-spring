package com.pm.server.controller;

import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/gamestate")
public class GameStateController {

	@Autowired
	private GameStateRegistry gameStateRegistry;

	@Autowired
	private PacdotRegistry pacdotRegistry;

	private final static Logger log =
			LogManager.getLogger(GameStateController.class.getName());

	@RequestMapping(
			value="/score",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<ScoreResponse> getGameScore() {

		log.info("Mapped GET /gamestate/score");

		Integer score = 0;
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			if(pacdot.getEaten()) {
				if(pacdot.getPowerdot()) {
					score += 50;
				}
				else {
					score += 10;
				}
			}
		}
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
	public ResponseEntity<GameStateResponse> getGamestate(HttpServletResponse response) {
		log.info("Mapped GET /gamestate");

		GameStateResponse stateResponse = new GameStateResponse();
		stateResponse.setState(gameStateRegistry.getCurrentState());

		return ResponseEntity.status(HttpStatus.OK).body(stateResponse);
	}

}