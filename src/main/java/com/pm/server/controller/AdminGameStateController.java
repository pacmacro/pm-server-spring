package com.pm.server.controller;

import com.pm.server.PmServerException;
import com.pm.server.datatype.GameState;
import com.pm.server.manager.AdminGameStateManager;
import com.pm.server.request.StateRequest;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/gamestate")
public class AdminGameStateController {

	@Autowired
	private AdminGameStateManager adminGameStateManager;

	private final static Logger log =
			LogManager.getLogger(AdminGameStateController.class.getName());

	@RequestMapping(
			value = "",
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

		adminGameStateManager.changeGameState(newState);

		return ResponseEntity.status(HttpStatus.OK).body(null);

	}

}
