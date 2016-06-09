package com.pm.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.server.player.Ghost;
import com.pm.server.player.GhostRepository;
import com.pm.server.response.GhostResponse;
import com.pm.server.response.GhostsResponse;

@RestController
@RequestMapping("/ghost")
public class GhostControllerImpl implements GhostController {

	@Autowired
	private GhostRepository ghostRepository;

	private final static Logger log =
			LogManager.getLogger(GhostControllerImpl.class.getName());

	private final static ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(
			value="/{id}/location",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public GhostResponse getLocationById(
			@PathVariable Integer id,
			HttpServletResponse response) {

		if(id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		log.debug("Mapped /ghosts/{}/location", Integer.toString(id));

		Ghost ghost = ghostRepository.getGhostById(id);
		if(ghost == null) {
			log.debug("No ghost with id {}", Integer.toString(id));
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		GhostResponse ghostResponse = new GhostResponse();
		ghostResponse.setId(ghost.getId());
		ghostResponse.setLocation(ghost.getLocation());

		try {
			log.debug(
					"Returning ghostResponse: {}",
					objectMapper.writeValueAsString(ghostResponse)
			);
		}
		catch (Exception e) {
			log.debug(e);
		}

		return ghostResponse;
	}

	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public GhostsResponse getAllLocations() {

		log.debug("Mapped /ghosts/location");

		GhostsResponse ghostsResponse = new GhostsResponse();

		List<Ghost> ghosts = ghostRepository.getAllGhosts();

		if(ghosts != null) {
			for(Ghost ghost : ghosts) {

				try {
					log.debug(
							"Processing ghost: {}",
							objectMapper.writeValueAsString(ghost)
					);
				}
				catch (Exception e) {
					log.debug(e);
				}

				GhostResponse ghostResponse = new GhostResponse();
				ghostResponse.setId(ghost.getId());
				ghostResponse.setLocation(ghost.getLocation());

				ghostsResponse.addGhostResponse(ghostResponse);
			}
		}

		try {
			log.debug(
					"Returning ghostsResponse: {}",
					objectMapper.writeValueAsString(ghostsResponse)
			);
		}
		catch (Exception e) {
			log.debug(e);
		}

		return ghostsResponse;
	}

}