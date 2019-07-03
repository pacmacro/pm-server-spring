package com.pm.server.controller;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Player;
import com.pm.server.manager.PlayerManager;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.request.LocationRequest;
import com.pm.server.response.*;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/player")
@SuppressWarnings("unused")
public class PlayerController {

	private PlayerManager playerManager;

	@Autowired
	public PlayerController(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	private final static Logger log =
			LogManager.getLogger(PlayerController.class.getName());

	@RequestMapping(
			value = "/{playerName}",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity selectPlayer(
			@PathVariable String playerName,
			@RequestBody(required = false) LocationRequest requestBody
			) throws PmServerException {

		log.info("Mapped POST /player/{}", playerName);
		log.info("Request body: {}", JsonUtils.objectToJson(requestBody));

		Player.Name name = ValidationUtils.validateRequestWithName(playerName);
		Coordinate location = ValidationUtils
				.validateRequestBodyWithLocation(requestBody);

		log.info("Attempting to select Player {} at ({}, {}).",
				name,
				location.getLatitude(),
				location.getLongitude()
		);

		playerManager.selectPlayer(name, location);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@RequestMapping(
			value="/{playerName}",
			method=RequestMethod.DELETE
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity deselectPlayer(
			@PathVariable String playerName)
			throws PmServerException {

		log.info("Mapped DELETE /player/{}", playerName);

		Player.Name name = ValidationUtils.validateRequestWithName(playerName);

		playerManager.setPlayerState(name, Player.State.UNINITIALIZED);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@RequestMapping(
			value="/{playerName}/location",
			method=RequestMethod.PUT
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity setPlayerLocation(
			@PathVariable String playerName,
			@RequestBody LocationRequest locationRequest)
			throws PmServerException {

		log.info("Mapped PUT /player/{}/location", playerName);
		log.info("Request body: {}", JsonUtils.objectToJson(locationRequest));

		Player.Name name = ValidationUtils.validateRequestWithName(playerName);
		Coordinate location = ValidationUtils
				.validateRequestBodyWithLocation(locationRequest);

		log.info(
				"Setting Player {} to ({}, {})",
				name, location.getLatitude(), location.getLongitude()
		);
		playerManager.setPlayerLocation(name, location);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@RequestMapping(
			value="/{playerName}/location",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<LocationResponse> getPlayerLocation(
			@PathVariable String playerName)
			throws PmServerException {

		log.info("Mapped GET /player/{}/location", playerName);

		Player.Name name = ValidationUtils.validateRequestWithName(playerName);

		LocationResponse response = new LocationResponse(
				playerManager.getPlayerLocation(name)
		);

		String objectString = JsonUtils.objectToJson(response);
		if(objectString != null) {
			log.debug("Returning locationResponse: {}", objectString);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PlayerNameAndLocationResponse>>
			getAllPlayerLocations() {

		log.info("Mapped GET /player/locations");

		List<PlayerNameAndLocationResponse> response =
				playerManager.getAllPlayerLocations();

		String objectString = JsonUtils.objectToJson(response);
		if(objectString != null) {
			log.debug("Returning Player response list: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@RequestMapping(
			value="/{playerName}/state",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<PlayerStateResponse> getPlayerState(
			@PathVariable String playerName)
			throws PmServerException {

		log.info("Mapped GET /player/{}/state", playerName);

		Player.Name name = ValidationUtils.validateRequestWithName(playerName);

		PlayerStateResponse response = new PlayerStateResponse(
				playerManager.getPlayerState(name)
		);

		String objectString = JsonUtils.objectToJson(response);
		if(objectString != null) {
			log.info(
					"Returning Player " + playerName +
					" with state {}", objectString
			);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@RequestMapping(
			value="/states",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PlayerNameAndPlayerStateResponse>>
			getAllPlayerStates() {

		log.info("Mapped GET /player/states");

		List<PlayerNameAndPlayerStateResponse> response =
				playerManager.getAllPlayerStates();

		String objectString = JsonUtils.objectToJson(response);
		if(objectString != null) {
			log.debug("Returning Player states: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@RequestMapping(
			value="/details",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PlayerDetailsResponse>>
			getAllPlayerDetails() {

		log.info("Mapped GET /player/details");

		List<PlayerDetailsResponse> response =
				playerManager.getAllPlayerDetails();

		String objectString = JsonUtils.objectToJson(response);
		if(objectString != null) {
			log.debug("Returning player details: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}