package com.pm.server.controller;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;
import com.pm.server.manager.PacdotManager;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.request.StateRequest;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private PacdotManager pacdotManager;

	@Autowired
	private PlayerRegistry playerRegistry;

	private final static Logger log =
			LogManager.getLogger(AdminController.class.getName());

	@RequestMapping(
			value="/pacdots/reset",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity resetPacdots() {
		log.info("Mapped POST /admin/pacdots/reset");

		pacdotManager.resetPacdots();
		log.info("All pacdots reset.");

		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	@RequestMapping(
			value="/player/{playerName}/state",
			method=RequestMethod.PUT
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity setPlayerState(
			@PathVariable String playerName,
			@RequestBody StateRequest stateRequest)
			throws PmServerException {

		log.info("Mapped PUT /admin/player/{}/state", playerName);
		log.info("Request body: {}", JsonUtils.objectToJson(stateRequest));

		Player.Name name = ValidationUtils.validateRequestWithName(playerName);

		Player.State newState =
				ValidationUtils.validateRequestBodyWithState(stateRequest);

		Player.State currentState = playerRegistry.getPlayerState(name);

		// Illegal state changes
		if(currentState == Player.State.UNINITIALIZED &&
				currentState != newState) {
			String errorMessage =
					"This operation cannot change the state of an unselected/" +
							"uninitialized player; use POST /player/{playerName} " +
							"instead.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
		}
		else if(newState == Player.State.UNINITIALIZED &&
				newState != currentState) {
			String errorMessage =
					"This operation cannot change the state of a selected/" +
							"initialized player to uninitialized; use " +
							"DELETE /player/{playerName} instead.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
		}

		// Illegal player states
		if(name != Player.Name.Pacman &&
				newState == Player.State.POWERUP) {
			String errorMessage = "The POWERUP state is not valid for a Ghost.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
		}

		log.info(
				"Changing Player {} from state {} to {}",
				name, currentState, newState
		);
		playerRegistry.setPlayerStateByName(name, newState);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
