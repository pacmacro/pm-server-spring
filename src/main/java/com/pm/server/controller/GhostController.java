package com.pm.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.player.Ghost;
import com.pm.server.player.GhostRepository;
import com.pm.server.response.GhostResponse;
import com.pm.server.utils.JsonUtils;

@RestController
@RequestMapping("/ghost")
public class GhostController {

	@Autowired
	private GhostRepository ghostRepository;

	private final static Logger log =
			LogManager.getLogger(GhostController.class.getName());

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
	public List<GhostResponse> getAllLocations() {

		log.debug("Mapped /ghosts/location");

		List<GhostResponse> ghostResponseList = new ArrayList<GhostResponse>();

		List<Ghost> ghosts = ghostRepository.getAllGhosts();

		if(ghosts != null) {
			for(Ghost ghost : ghosts) {

				String objectString = JsonUtils.objectToJson(ghost);
				if(objectString != null) {
					log.debug("Processing ghost: {}", objectString);
				}

				GhostResponse ghostResponse = new GhostResponse();
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