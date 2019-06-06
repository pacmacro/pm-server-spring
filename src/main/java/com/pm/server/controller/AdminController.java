package com.pm.server.controller;

import com.pm.server.PmServerException;
import com.pm.server.datatype.GameState;
import com.pm.server.datatype.Player;
import com.pm.server.manager.AdminManager;
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
@SuppressWarnings("unused")
public class AdminController {

	private AdminManager adminManager;

	private final static Logger log =
			LogManager.getLogger(AdminController.class.getName());

	@Autowired
	public AdminController(AdminManager adminManager) {
		this.adminManager = adminManager;
	}

	@RequestMapping(
			value = "/gamestate",
			method = RequestMethod.PUT,
			produces = { "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity changeGameState(@RequestBody StateRequest requestBody)
			throws PmServerException {

		log.info("Mapped PUT /admin/gamestate");
		log.info("Request body: {}", JsonUtils.objectToJson(requestBody));

		GameState newState =
				ValidationUtils.validateRequestBodyWithGameState(requestBody);

		try {
			adminManager.changeGameState(newState);
		}
		catch(IllegalStateException e) {
			throw new PmServerException(HttpStatus.CONFLICT, e.getMessage());
		}

		return ResponseEntity.status(HttpStatus.OK).body(null);

	}

	@RequestMapping(
			value="/pacdots/reset",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity resetPacdots() {
		log.info("Mapped POST /admin/pacdots/reset");

		adminManager.resetPacdots();
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

		adminManager.setPlayerState(name, newState);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
