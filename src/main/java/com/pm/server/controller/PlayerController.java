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
import com.pm.server.request.PlayerNameAndLocationRequest;
import com.pm.server.request.PlayerNameRequest;
import com.pm.server.request.PlayerStateRequest;
import com.pm.server.response.IdAndPlayerStateResponse;
import com.pm.server.response.LocationResponse;
import com.pm.server.response.PlayerResponse;
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
			value = "",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void selectPlayer(
			@RequestBody(required = false)
			PlayerNameAndLocationRequest requestBody)
			throws ConflictException, BadRequestException {

		log.debug("Mapped POST /player");
		log.debug("Request body: {}", JsonUtils.objectToJson(requestBody));

		PlayerNameRequest nameRequest = new PlayerNameRequest();
		nameRequest.name = requestBody.name;
		PlayerName name = ValidationUtils.validateRequestBodyWithName(nameRequest);

		LocationRequest locationRequest = new LocationRequest();
		locationRequest.latitude = requestBody.location.latitude;
		locationRequest.longitude = requestBody.location.longitude;
		Coordinate location = ValidationUtils
				.validateRequestBodyWithLocation(locationRequest);

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
			value="/{id}",
			method=RequestMethod.DELETE
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void deletePlayerById(
			@PathVariable Integer id,
			HttpServletResponse response)
			throws NotFoundException, InternalServerErrorException {

		log.debug("Mapped DELETE /player/{}", id);

		Player player = playerRegistry.getPlayerByName(PlayerName.Inky);
		if(player == null) {
			String errorMessage =
					"Player with id " +
					Integer.toString(id) +
					" was not found.";
			log.warn(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		try {
			throw new Exception();
			//playerRegistry.deletePlayerByName(PlayerName.Inky);
		}
		catch(Exception e) {
			String errorMessage =
					"Player with id " +
					Integer.toString(id) +
					" was found but could not be deleted.";
			log.warn(errorMessage);
			throw new InternalServerErrorException(errorMessage);
		}

		//log.debug("Player with id {} was deleted", id);
	}

	@RequestMapping(
			value="/{id}/location",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public LocationResponse getPlayerLocationById(
			@PathVariable Integer id,
			HttpServletResponse response)
			throws NotFoundException {

		log.debug("Mapped GET /player/{}/location", id);

		Player player = playerRegistry.getPlayerByName(PlayerName.Inky);
		if(player == null) {
			String errorMessage =
					"No Player with id " +
					Integer.toString(id) +
					".";
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
	public List<PlayerResponse> getAllPlayerLocations() {

		log.debug("Mapped GET /player/locations");

		List<PlayerResponse> playerResponseList = new ArrayList<PlayerResponse>();

		List<Player> playerList = playerRegistry.getAllPlayers();

		if(playerList != null) {
			for(Player player : playerList) {

				String objectString = JsonUtils.objectToJson(player);
				if(objectString != null) {
					log.trace("Processing Player: {}", objectString);
				}

				PlayerResponse playerResponse = new PlayerResponse();
				playerResponse.setId(player.getId());
				playerResponse.setLocation(player.getLocation());

				playerResponseList.add(playerResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning PlayersResponse: {}", objectString);
		}

		return playerResponseList;
	}

	@RequestMapping(
			value="/{id}/state",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public PlayerStateResponse getPlayerStateById(
			@PathVariable Integer id,
			HttpServletResponse response)
			throws NotFoundException {

		log.debug("Mapped GET /player/{}/state", id);

		Player player = playerRegistry.getPlayerByName(PlayerName.Inky);
		if(player == null) {
			String errorMessage =
					"No Player with id " +
					Integer.toString(id) +
					".";
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
	public List<IdAndPlayerStateResponse> getAllPlayerStates() {

		log.debug("Mapped GET /player/states");

		List<IdAndPlayerStateResponse> playerResponseList =
				new ArrayList<IdAndPlayerStateResponse>();

		List<Player> players = playerRegistry.getAllPlayers();

		if(players != null) {
			for(Player player : players) {

				String objectString = JsonUtils.objectToJson(player);
				if(objectString != null) {
					log.trace("Processing Player: {}", objectString);
				}

				IdAndPlayerStateResponse PlayerResponse = new IdAndPlayerStateResponse();
				PlayerResponse.id = player.getId();
				PlayerResponse.state = player.getState();

				playerResponseList.add(PlayerResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(playerResponseList);
		if(objectString != null) {
			log.debug("Returning playerResponse: {}", objectString);
		}

		return playerResponseList;
	}

	@RequestMapping(
			value="/{id}/location",
			method=RequestMethod.PUT
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void setPlayerLocationById(
			@PathVariable Integer id,
			@RequestBody LocationRequest locationRequest)
			throws BadRequestException, NotFoundException {

		log.debug("Mapped PUT /player/{}/location", id);
		log.debug("Request body: {}", JsonUtils.objectToJson(locationRequest));

		Coordinate location = ValidationUtils
				.validateRequestBodyWithLocation(locationRequest);

		Player player = playerRegistry.getPlayerByName(PlayerName.Inky);
		if(player == null) {
			String errorMessage =
					"Player with id " +
					Integer.toString(id) +
					" was not found.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		log.debug(
				"Setting Player with id {} to ({}, {})",
				id, location.getLatitude(), location.getLongitude()
		);
		playerRegistry.setPlayerLocationByName(PlayerName.Inky, location);
	}

	@RequestMapping(
			value="/{id}/state",
			method=RequestMethod.PUT
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void setPlayerStateById(
			@PathVariable Integer id,
			@RequestBody PlayerStateRequest stateRequest)
			throws BadRequestException, NotFoundException {

		log.debug("Mapped PUT /player/{}/state", id);
		log.debug("Request body: {}", JsonUtils.objectToJson(stateRequest));

		PlayerState state =
				ValidationUtils.validateRequestBodyWithState(stateRequest);

		if(state == PlayerState.POWERUP) {
			String errorMessage = "The POWERUP state is not valid for a Player.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		Player player = playerRegistry.getPlayerByName(PlayerName.Inky);
		if(player == null) {
			String errorMessage =
					"Player with id " +
					Integer.toString(id) +
					" was not found.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		log.debug(
				"Changing Player with id {} from state {} to {}",
				id, player.getState(), state
		);
		playerRegistry.setPlayerStateByName(PlayerName.Inky, state);

	}

}