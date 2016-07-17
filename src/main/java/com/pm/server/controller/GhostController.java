package com.pm.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.datatype.PlayerState;
import com.pm.server.exceptionhttp.BadRequestException;
import com.pm.server.exceptionhttp.ConflictException;
import com.pm.server.exceptionhttp.InternalServerErrorException;
import com.pm.server.exceptionhttp.NotFoundException;
import com.pm.server.player.Ghost;
import com.pm.server.player.GhostImpl;
import com.pm.server.repository.GhostRepository;
import com.pm.server.request.PlayerStateRequest;
import com.pm.server.response.IdAndPlayerStateResponse;
import com.pm.server.response.IdResponse;
import com.pm.server.response.PlayerResponse;
import com.pm.server.response.PlayerStateResponse;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;

@RestController
@RequestMapping("/ghost")
public class GhostController {

	@Autowired
	private GhostRepository ghostRepository;

	private final static Logger log =
			LogManager.getLogger(GhostController.class.getName());

	@RequestMapping(
			value = "",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public IdResponse createGhost(
			@RequestBody(required = false) CoordinateImpl location)
			throws ConflictException, BadRequestException {

		log.debug("Mapped POST /ghost");
		log.debug("Request body: {}", JsonUtils.objectToJson(location));

		ValidationUtils.validateRequestBodyWithLocation(location);

		log.debug("Creating Ghost at ({}, {}).",
				location.getLatitude(),
				location.getLongitude()
		);

		Ghost ghost = new GhostImpl();
		ghost.setLocation(location);

		Random random = new Random();

		Boolean createdGhost = false;
		final Integer maxGhostId = ghostRepository.maxGhostId();
		final Integer idCreationRetries = maxGhostId / 2;
		for(Integer i = 0; i < idCreationRetries; i++) {

			if(createdGhost) {
				break;
			}
			createdGhost = true;

			ghost.setId(random.nextInt(maxGhostId));
			try {
				ghostRepository.addPlayer(ghost);
			}
			catch (Exception e) {
				createdGhost = false;
				log.warn(e.getMessage());
			}

		}

		if(!createdGhost) {
			String errorMessage;
			String objectString = JsonUtils.objectToJson(ghost);
			if(objectString != null) {
				errorMessage =
						"Ghost " +
						objectString +
						"could not be created.";
			}
			else {
				errorMessage = "Ghost could not be created.";
			}

			log.error(errorMessage);
			throw new ConflictException(errorMessage);
		}

		log.debug("Ghost id set to {}.", ghost.getId());

		IdResponse idResponse = new IdResponse();
		idResponse.setId(ghost.getId());
		return idResponse;
	}

	@RequestMapping(
			value="/{id}",
			method=RequestMethod.DELETE
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteGhostById(
			@PathVariable Integer id,
			HttpServletResponse response)
			throws NotFoundException, InternalServerErrorException {

		log.debug("Mapped DELETE /ghost/{}", id);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			String errorMessage =
					"Ghost with id " +
					Integer.toString(id) +
					" was not found.";
			log.warn(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		try {
			ghostRepository.deletePlayerById(id);
		}
		catch(Exception e) {
			String errorMessage =
					"Ghost with id " +
					Integer.toString(id) +
					" was found but could not be deleted.";
			log.warn(errorMessage);
			throw new InternalServerErrorException(errorMessage);
		}

		log.debug("Ghost with id {} was deleted", id);
	}

	@RequestMapping(
			value="/{id}/location",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public PlayerResponse getGhostLocationById(
			@PathVariable Integer id,
			HttpServletResponse response)
			throws NotFoundException {

		log.debug("Mapped GET /ghost/{}/location", id);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			String errorMessage =
					"No ghost with id " +
					Integer.toString(id) +
					".";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		PlayerResponse ghostResponse = new PlayerResponse();
		ghostResponse.setId(ghost.getId());
		ghostResponse.setLocation(ghost.getLocation());

		String objectString = JsonUtils.objectToJson(ghostResponse);
		if(objectString != null) {
			log.debug("Returning ghostResponse: {}", objectString);
		}

		return ghostResponse;
	}

	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public List<PlayerResponse> getAllGhostLocations() {

		log.debug("Mapped GET /ghost/locations");

		List<PlayerResponse> ghostResponseList = new ArrayList<PlayerResponse>();

		List<Ghost> ghosts = ghostRepository.getAllPlayers();

		if(ghosts != null) {
			for(Ghost ghost : ghosts) {

				String objectString = JsonUtils.objectToJson(ghost);
				if(objectString != null) {
					log.trace("Processing ghost: {}", objectString);
				}

				PlayerResponse ghostResponse = new PlayerResponse();
				ghostResponse.setId(ghost.getId());
				ghostResponse.setLocation(ghost.getLocation());

				ghostResponseList.add(ghostResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(ghostResponseList);
		if(objectString != null) {
			log.debug("Returning ghostsResponse: {}", objectString);
		}

		return ghostResponseList;
	}

	@RequestMapping(
			value="/{id}/state",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public PlayerStateResponse getGhostStateById(
			@PathVariable Integer id,
			HttpServletResponse response)
			throws NotFoundException {

		log.debug("Mapped GET /ghost/{}/state", id);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			String errorMessage =
					"No ghost with id " +
					Integer.toString(id) +
					".";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		PlayerStateResponse playerStateResponse = new PlayerStateResponse();
		playerStateResponse.setState(ghost.getState());

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
	public List<IdAndPlayerStateResponse> getAllGhostStates() {

		log.debug("Mapped GET /ghost/states");

		List<IdAndPlayerStateResponse> ghostResponseList =
				new ArrayList<IdAndPlayerStateResponse>();

		List<Ghost> ghosts = ghostRepository.getAllPlayers();

		if(ghosts != null) {
			for(Ghost ghost : ghosts) {

				String objectString = JsonUtils.objectToJson(ghost);
				if(objectString != null) {
					log.trace("Processing ghost: {}", objectString);
				}

				IdAndPlayerStateResponse ghostResponse = new IdAndPlayerStateResponse();
				ghostResponse.id = ghost.getId();
				ghostResponse.state = ghost.getState();

				ghostResponseList.add(ghostResponse);
			}
		}

		String objectString = JsonUtils.objectToJson(ghostResponseList);
		if(objectString != null) {
			log.debug("Returning ghostsResponse: {}", objectString);
		}

		return ghostResponseList;
	}

	@RequestMapping(
			value="/{id}/location",
			method=RequestMethod.PUT
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void setGhostLocationById(
			@PathVariable Integer id,
			@RequestBody CoordinateImpl location)
			throws BadRequestException, NotFoundException {

		log.debug("Mapped PUT /ghost/{}/location", id);
		log.debug("Request body: {}", JsonUtils.objectToJson(location));

		ValidationUtils.validateRequestBodyWithLocation(location);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			String errorMessage =
					"Ghost with id " +
					Integer.toString(id) +
					" was not found.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		log.debug(
				"Setting ghost with id {} to ({}, {})",
				id, location.getLatitude(), location.getLongitude()
		);
		ghostRepository.setPlayerLocationById(id, location);
	}

	@RequestMapping(
			value="/{id}/state",
			method=RequestMethod.PUT
	)
	@ResponseStatus(value = HttpStatus.OK)
	public void setGhostStateById(
			@PathVariable Integer id,
			@RequestBody PlayerStateRequest stateRequest)
			throws BadRequestException, NotFoundException {

		log.debug("Mapped PUT /ghost/{}/state", id);
		log.debug("Request body: {}", JsonUtils.objectToJson(stateRequest));

		PlayerState state =
				ValidationUtils.validateRequestBodyWithState(stateRequest);

		if(state == PlayerState.POWERUP) {
			String errorMessage = "The POWERUP state is not valid for a Ghost.";
			log.warn(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			String errorMessage =
					"Ghost with id " +
					Integer.toString(id) +
					" was not found.";
			log.debug(errorMessage);
			throw new NotFoundException(errorMessage);
		}

		log.debug(
				"Changing ghost with id {} from state {} to {}",
				id, ghost.getState(), state
		);
		ghostRepository.setPlayerStateById(id, state);

	}

}