package com.pm.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;
import com.pm.server.exceptionhttp.BadRequestException;
import com.pm.server.exceptionhttp.ConflictException;
import com.pm.server.exceptionhttp.InternalServerErrorException;
import com.pm.server.exceptionhttp.NotFoundException;
import com.pm.server.player.Player;
import com.pm.server.registry.PlayerRegistry;
import com.pm.server.request.LocationRequest;
import com.pm.server.request.PlayerNameRequest;
import com.pm.server.request.PlayerStateRequest;
import com.pm.server.response.LocationResponse;
import com.pm.server.response.PlayerDetailsResponse;
import com.pm.server.response.PlayerNameAndLocationResponse;
import com.pm.server.response.PlayerNameAndPlayerStateResponse;
import com.pm.server.response.PlayerStateResponse;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;

@RestController
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	private PlayerRegistry playerRegistry;

	private final static Logger log =
			LogManager.getLogger(PlayerController.class.getName());

	@RequestMapping(
			value = "/{playerName}",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void selectPlayer(
			@PathVariable String playerName,
			@RequestBody(required = false)
			LocationRequest requestBody)
			throws
			BadRequestException,
			ConflictException,
			NotFoundException {

		log.debug("Mapped POST /player/{}", playerName);
		log.debug("Request body: {}", JsonUtils.objectToJson(requestBody));

		PlayerNameRequest playerNameRequest = new PlayerNameRequest();
		playerNameRequest.name = playerName;
		PlayerName name =
				ValidationUtils.validateRequestWithName(playerNameRequest);

		Coordinate location = ValidationUtils
				.validateRequestBodyWithLocation(requestBody);

		log.debug("Attempting to select Player {} at ({}, {}).",
				name,
				location.getLatitude(),
				location.getLongitude()
		);

		Player player = playerRegistry.getPlayerByName(name);
		if(player == null) {
			String errorMessage = name + " is not a valid Player name.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}
		else if(player.getState() != PlayerState.UNINITIALIZED) {
			String errorMessage =
					"Player "+
					name +
					" has already been selected.";
			log.warn(errorMessage);
			throw new ConflictException(errorMessage);
		}

		playerRegistry.setPlayerLocationByName(name, location);
		playerRegistry.setPlayerStateByName(name, PlayerState.READY);

	}

	@RequestMapping(
			value="/{playerName}",
			method=RequestMethod.DELETE
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void deselectPlayer(
			@PathVariable String playerName,
			HttpServletResponse response)
			throws BadRequestException,
			NotFoundException,
			InternalServerErrorException {

		log.debug("Mapped DELETE /player/{}", playerName);

		PlayerNameRequest playerNameRequest = new PlayerNameRequest();
		playerNameRequest.name = playerName;
		PlayerName name = ValidationUtils
				.validateRequestWithName(playerNameRequest);

		Player player = playerRegistry.getPlayerByName(name);
		if(player == null) {
			String errorMessage =
					"Player " +
					name +
					" was not found.";
			log.warn(errorMessage);
			throw new NotFoundException(errorMessage);
		}
		else if(player.getState() == PlayerState.UNINITIALIZED) {
			String errorMessage =
					"Player "+
					name +
					" has not yet been selected.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		try {
			playerRegistry.setPlayerStateByName(
					name, PlayerState.UNINITIALIZED
			);
		}
		catch(Exception e) {
			String errorMessage =
					"Player " +
					name +
					" was found but could not be deselected.";
			log.warn(errorMessage);
			throw new InternalServerErrorException(errorMessage);
		}
		log.debug("Player {} was succesfully deselected", name);

		log.debug("Setting Player {} to default location", name);
		player.resetLocation();

	}

	@RequestMapping(
			value="/{playerName}/location",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public LocationResponse getPlayerLocation(
			@PathVariable String playerName,
			HttpServletResponse response)
			throws BadRequestException, NotFoundException {

		log.debug("Mapped GET /player/{}/location", playerName);

		PlayerNameRequest nameRequest = new PlayerNameRequest();
		nameRequest.name = playerName;
		PlayerName name = ValidationUtils
				.validateRequestWithName(nameRequest);

		Player player = playerRegistry.getPlayerByName(name);
		if(player == null) {
			String errorMessage =
					"No Player named " +
					name +
					" was found in the registry.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		LocationResponse locationResponse = new LocationResponse();
		locationResponse.setLatitude(player.getLocation().getLatitude());
		locationResponse.setLongitude(player.getLocation().getLongitude());

		String objectString = JsonUtils.objectToJson(locationResponse);
		if(objectString != null) {
			log.debug("Returning locationResponse: {}", objectString);
		}

		return locationResponse;
	}

	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public List<PlayerNameAndLocationResponse> getAllPlayerLocations() {

		log.debug("Mapped GET /player/locations");

		List<PlayerNameAndLocationResponse> playerResponseList = new ArrayList<PlayerNameAndLocationResponse>();

		List<Player> playerList = playerRegistry.getAllPlayers();

		if(playerList != null) {
			for(Player player : playerList) {

				String objectString = JsonUtils.objectToJson(player);
				if(objectString != null) {
					log.trace("Processing Player: {}", objectString);
				}

				PlayerNameAndLocationResponse playerResponse = new PlayerNameAndLocationResponse();
				playerResponse.setName(player.getName());
				playerResponse.setLocation(player.getLocation());

				playerResponseList.add(playerResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning Player response list: {}", objectString);
		}

		return playerResponseList;
	}

	@RequestMapping(
			value="/{playerName}/state",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public PlayerStateResponse getPlayerState(
			@PathVariable String playerName,
			HttpServletResponse response)
			throws BadRequestException, NotFoundException {

		log.debug("Mapped GET /player/{}/state", playerName);

		PlayerNameRequest nameRequest = new PlayerNameRequest();
		nameRequest.name = playerName;
		PlayerName name = ValidationUtils.validateRequestWithName(nameRequest);

		Player player = playerRegistry.getPlayerByName(name);
		if(player == null) {
			String errorMessage =
					"No Player named " +
					name +
					" was found in the registry.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		PlayerStateResponse playerStateResponse = new PlayerStateResponse();
		playerStateResponse.setState(player.getState());

		String objectString = JsonUtils.objectToJson(playerStateResponse);
		if(objectString != null) {
			log.debug("Returning playerStateResponse: {}", objectString);
		}

		return playerStateResponse;
	}

	@RequestMapping(
			value="/states",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public List<PlayerNameAndPlayerStateResponse> getAllPlayerStates() {

		log.debug("Mapped GET /player/states");

		List<PlayerNameAndPlayerStateResponse> playerResponseList =
				new ArrayList<PlayerNameAndPlayerStateResponse>();

		List<Player> players = playerRegistry.getAllPlayers();

		if(players != null) {
			for(Player player : players) {

				String objectString = JsonUtils.objectToJson(player);
				if(objectString != null) {
					log.trace("Processing Player: {}", objectString);
				}

				PlayerNameAndPlayerStateResponse playerResponse = new PlayerNameAndPlayerStateResponse();
				playerResponse.name = player.getName();
				playerResponse.state = player.getState();

				playerResponseList.add(playerResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning playerResponse: {}", objectString);
		}

		return playerResponseList;
	}

	@RequestMapping(
			value="/details",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public List<PlayerDetailsResponse> getAllPlayerDetails() {

		log.debug("Mapped GET /player/details");

		List<PlayerDetailsResponse> playerResponseList =
				new ArrayList<PlayerDetailsResponse>();

		List<Player> players = playerRegistry.getAllPlayers();

		if(players != null) {
			for(Player player : players) {

				String objectString = JsonUtils.objectToJson(player);
				if(objectString != null) {
					log.trace("Processing Player: {}", objectString);
				}

				PlayerDetailsResponse playerResponse = new PlayerDetailsResponse();
				playerResponse.setName(player.getName());
				playerResponse.setState(player.getState());
				playerResponse.setLocation(player.getLocation());

				playerResponseList.add(playerResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning playerResponse: {}", objectString);
		}

		return playerResponseList;
	}

	@RequestMapping(
			value="/{playerName}/location",
			method=RequestMethod.PUT
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void setPlayerLocation(
			@PathVariable String playerName,
			@RequestBody LocationRequest locationRequest)
			throws BadRequestException,
			ConflictException,
			NotFoundException {

		log.debug("Mapped PUT /player/{}/location", playerName);
		log.debug("Request body: {}", JsonUtils.objectToJson(locationRequest));

		PlayerNameRequest nameRequest = new PlayerNameRequest();
		nameRequest.name = playerName;
		PlayerName name = ValidationUtils.validateRequestWithName(nameRequest);

		Coordinate location = ValidationUtils
				.validateRequestBodyWithLocation(locationRequest);

		Player player = playerRegistry.getPlayerByName(name);
		if(player == null) {
			String errorMessage =
					"Player " +
					name +
					" was not found.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}
		else if(player.getState() == PlayerState.UNINITIALIZED) {
			String errorMessage =
					"Player " +
			        name +
			        " has not been selected yet, so a location cannot be set.";
			log.warn(errorMessage);
			throw new ConflictException(errorMessage);
		}

		log.debug(
				"Setting Player {} to ({}, {})",
				name, location.getLatitude(), location.getLongitude()
		);
		playerRegistry.setPlayerLocationByName(name, location);
	}

	@RequestMapping(
			value="/{playerName}/state",
			method=RequestMethod.PUT
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void setPlayerState(
			@PathVariable String playerName,
			@RequestBody PlayerStateRequest stateRequest)
			throws BadRequestException,
			ConflictException,
			NotFoundException {

		log.debug("Mapped PUT /player/{}/state", playerName);
		log.debug("Request body: {}", JsonUtils.objectToJson(stateRequest));

		PlayerNameRequest nameRequest = new PlayerNameRequest();
		nameRequest.name = playerName;
		PlayerName name = ValidationUtils.validateRequestWithName(nameRequest);

		PlayerState state =
				ValidationUtils.validateRequestBodyWithState(stateRequest);

		Player player = playerRegistry.getPlayerByName(name);
		if(player == null) {
			String errorMessage =
					"Player " +
					name +
					" was not found.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		// Illegal state changes
		if(player.getState() == PlayerState.UNINITIALIZED &&
				player.getState() != state) {
			String errorMessage =
					"This operation cannot change the state of an unselected/" +
					"uninitialized player; use POST /player/{playerName} " +
					"instead.";
			log.warn(errorMessage);
			throw new ConflictException(errorMessage);
		}
		else if(state == PlayerState.UNINITIALIZED &&
				state != player.getState()) {
			String errorMessage =
					"This operation cannot change the state of a selected/" +
					"initialized player to uninitialized; use " +
					"DELETE /player/{playerName} instead.";
			log.warn(errorMessage);
			throw new ConflictException(errorMessage);
		}

		// Illegal player states
		if(player.getName() != PlayerName.Pacman &&
				state == PlayerState.POWERUP) {
			String errorMessage = "The POWERUP state is not valid for a Ghost.";
			log.warn(errorMessage);
			throw new ConflictException(errorMessage);
		}

		log.debug(
				"Changing Player {} from state {} to {}",
				name, player.getState(), state
		);
		playerRegistry.setPlayerStateByName(name, state);

	}

}