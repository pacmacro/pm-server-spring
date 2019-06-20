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

	private PlayerRegistry playerRegistry;
	private PlayerManager playerManager;

	@Autowired
	public PlayerController(PlayerRegistry playerRegistry, PlayerManager playerManager) {
		this.playerRegistry = playerRegistry;
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

		if(playerRegistry.getPlayerState(name) == Player.State.UNINITIALIZED) {
			String errorMessage =
					"Player "+
					name +
					" has not yet been selected.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.BAD_REQUEST, errorMessage);
		}

		try {
			playerRegistry.setPlayerStateByName(
					name, Player.State.UNINITIALIZED
			);
		}
		catch(Exception e) {
			String errorMessage =
					"Player " +
					name +
					" could not be deselected.";
			log.warn(errorMessage);
			throw new PmServerException(
					HttpStatus.INTERNAL_SERVER_ERROR, errorMessage
			);
		}
		log.info("Player {} was succesfully deselected", name);

		log.debug("Setting Player {} to default location", name);
		playerRegistry.resetLocationOf(name);

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
		Coordinate location = playerRegistry.getPlayerLocation(name);

		LocationResponse locationResponse = new LocationResponse();
		locationResponse.setLatitude(location.getLatitude());
		locationResponse.setLongitude(location.getLongitude());

		String objectString = JsonUtils.objectToJson(locationResponse);
		if(objectString != null) {
			log.debug("Returning locationResponse: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(locationResponse);
	}

	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PlayerNameAndLocationResponse>>
			getAllPlayerLocations() {

		log.info("Mapped GET /player/locations");

		List<PlayerNameAndLocationResponse> playerResponseList =
				new ArrayList<>();

		for(Player.Name name : Player.Name.values()) {
			log.trace("Processing Player {}", name);
			playerResponseList.add(new PlayerNameAndLocationResponse(
					name, playerRegistry.getPlayerLocation(name)
			));
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning Player response list: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(playerResponseList);
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

		PlayerStateResponse playerStateResponse = new PlayerStateResponse();
		playerStateResponse.setState(playerRegistry.getPlayerState(name));

		String objectString = JsonUtils.objectToJson(playerStateResponse);
		if(objectString != null) {
			log.info(
					"Returning Player " + playerName +
					" with state {}", objectString
			);
		}

		return ResponseEntity.status(HttpStatus.OK).body(playerStateResponse);
	}

	@RequestMapping(
			value="/states",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PlayerNameAndPlayerStateResponse>>
			getAllPlayerStates() {

		log.info("Mapped GET /player/states");

		List<PlayerNameAndPlayerStateResponse> playerResponseList =
				new ArrayList<>();

		for(Player.Name name : Player.Name.values()) {
			log.trace("Processing Player {}", name);
			playerResponseList.add(new PlayerNameAndPlayerStateResponse(
					name, playerRegistry.getPlayerState(name)
			));
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning Player states: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(playerResponseList);
	}

	@RequestMapping(
			value="/details",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public ResponseEntity<List<PlayerDetailsResponse>>
			getAllPlayerDetails() {

		log.info("Mapped GET /player/details");

		List<PlayerDetailsResponse> playerResponseList =
				new ArrayList<>();

		for(Player.Name name : Player.Name.values()) {
			log.trace("Processing Player {}", name);
			playerResponseList.add(new PlayerDetailsResponse(
					name,
					playerRegistry.getPlayerState(name),
					playerRegistry.getPlayerLocation(name)
			));
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning player details: {}", objectString);
		}

		return ResponseEntity.status(HttpStatus.OK).body(playerResponseList);
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

		if(playerRegistry.getPlayerState(name) == Player.State.UNINITIALIZED) {
			String errorMessage =
					"Player " +
			        name +
			        " has not been selected yet, so a location cannot be set.";
			log.warn(errorMessage);
			throw new PmServerException(HttpStatus.CONFLICT, errorMessage);
		}

		log.info(
				"Setting Player {} to ({}, {})",
				name, location.getLatitude(), location.getLongitude()
		);
		playerRegistry.setPlayerLocationByName(name, location);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}