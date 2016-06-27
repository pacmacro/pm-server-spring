package com.pm.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.player.Ghost;
import com.pm.server.player.GhostImpl;
import com.pm.server.repository.GhostRepository;
import com.pm.server.response.PlayerResponse;
import com.pm.server.response.IdResponse;
import com.pm.server.utils.JsonUtils;

@RestController
@RequestMapping("/ghost")
public class GhostController implements PlayerController {

	@Autowired
	private GhostRepository ghostRepository;

	private final static Logger log =
			LogManager.getLogger(GhostController.class.getName());

	@RequestMapping(
			value = "/{latitude}/{longitude}",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	public IdResponse createGhost(
			@PathVariable double latitude,
			@PathVariable double longitude,
			HttpServletResponse response) {

		log.debug("Mapped POST /ghost/{}/{}", latitude, longitude);

		log.debug("Creating Ghost at ({}, {}).", latitude, longitude);

		Ghost ghost = new GhostImpl();
		ghost.setLocation(new CoordinateImpl(latitude, longitude));

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
			String objectString = JsonUtils.objectToJson(ghost);
			log.error("Ghost {} could not be created", objectString);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return null;
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
	public void deleteGhostById(
			@PathVariable Integer id,
			HttpServletResponse response) {

		log.debug("Mapped DELETE /ghost/{}", id);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		try {
			ghostRepository.deletePlayerById(id);
		}
		catch(Exception e) {
			log.warn(
					"Ghost with id {} was found but could not be deleted",
					id
			);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		log.debug("Ghost with id {} was deleted", id);
	}

	@RequestMapping(
			value="/{id}/location/{latitude}/{longitude}",
			method=RequestMethod.PUT
	)
	public void setGhostLocationById(
			@PathVariable Integer id,
			@PathVariable double latitude,
			@PathVariable double longitude,
			HttpServletResponse response) {

		log.debug(
				"Mapped PUT /ghost/{}/location/{}/{}",
				id, latitude, longitude);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			log.debug("Ghost with id {} was not found", id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		log.debug(
				"Setting ghost with id {} to ({}, {})",
				id, latitude, longitude
		);
		ghostRepository.setPlayerLocationById(
				id,
				new CoordinateImpl(latitude, longitude)
		);
	}

	@RequestMapping(
			value="/{id}/location",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)

	public PlayerResponse getGhostLocationById(
			@PathVariable Integer id,
			HttpServletResponse response) {

		if(id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		log.debug("Mapped GET /ghost/{}/location", id);

		Ghost ghost = ghostRepository.getPlayerById(id);
		if(ghost == null) {
			log.debug("No ghost with id {}", id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
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

}