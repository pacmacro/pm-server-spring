package com.pm.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.PmServerException;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.request.StateRequest;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;

@RestController
@RequestMapping("/admin/gamestate")
public class AdminGameStateController {

	@Autowired
	private GameStateRegistry gameStateRegistry;

	@Autowired
	private PlayerRegistry playerRegistry;

	@Autowired
	private PacdotRegistry pacdotRegistry;

	private final static Logger log =
			LogManager.getLogger(AdminGameStateController.class.getName());

	@RequestMapping(
			value = "",
			method = RequestMethod.PUT,
			produces = { "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity changeGameState(
			@RequestBody StateRequest requestBody,
			HttpServletResponse response)
			throws PmServerException {

		log.info("Mapped PUT /admin/gamestate");
		log.info("Request body: {}", JsonUtils.objectToJson(requestBody));

		GameState newState =
				ValidationUtils.validateRequestBodyWithGameState(requestBody);
		try {
			switch(newState) {
				case INITIALIZING:
					gameStateRegistry.resetGame();
					playerRegistry.reset();
					pacdotRegistry.resetPacdots();
					break;
				case IN_PROGRESS:
					gameStateRegistry.startGame();
					playerRegistry.changePlayerStates(
							Player.State.READY, Player.State.ACTIVE
					);
					break;
				case PAUSED:
					gameStateRegistry.pauseGame();
					break;
				case FINISHED_PACMAN_WIN:
					gameStateRegistry.setWinnerPacman();
					break;
				case FINISHED_GHOSTS_WIN:
					gameStateRegistry.setWinnerGhosts();
					playerRegistry
							.getPlayerByName(Player.Name.Pacman)
							.setState(Player.State.CAPTURED);
					break;
			}
		}
		catch(IllegalStateException e) {
			throw new PmServerException(HttpStatus.CONFLICT, e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.OK).body(null);

	}

}
