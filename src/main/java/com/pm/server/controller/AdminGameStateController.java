package com.pm.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.GameStateContainer;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.exceptionhttp.BadRequestException;
import com.pm.server.exceptionhttp.ConflictException;
import com.pm.server.game.GameState;
import com.pm.server.registry.GameStateRegistry;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.request.StringStateContainer;
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
			value="",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public GameStateContainer resetPacdots(HttpServletResponse response) {

		log.debug("Mapped GET /admin/gamestate");

		GameStateContainer stateContainer = new GameStateContainer();
		stateContainer.setState(gameStateRegistry.getCurrentState());
		return stateContainer;

	}

	@RequestMapping(
			value = "",
			method = RequestMethod.PUT,
			produces = { "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void changeGameState(
			@RequestBody StringStateContainer requestBody,
			HttpServletResponse response)
			throws
			BadRequestException,
			ConflictException {

		log.debug("Mapped PUT /admin/gamestate");
		log.debug("Request body: {}", JsonUtils.objectToJson(requestBody));

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
							PlayerState.READY, PlayerState.ACTIVE
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
							.getPlayerByName(PlayerName.Pacman)
							.setState(PlayerState.CAPTURED);
					break;
			}
		}
		catch(IllegalStateException e) {
			throw new ConflictException(e.getMessage());
		}

	}

}
