package com.pm.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.player.Ghost;
import com.pm.server.player.GhostRepository;
import com.pm.server.response.GhostResponse;
import com.pm.server.response.GhostsResponse;

@RestController
@RequestMapping("/ghost")
public class GhostControllerImpl implements GhostController {

	@Autowired
	private GhostRepository ghostRepository;

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

		Ghost ghost = ghostRepository.getGhostById(id);
		if(ghost == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		GhostResponse ghostResponse = new GhostResponse();
		ghostResponse.setId(ghost.getId());
		ghostResponse.setLocation(ghost.getLocation());
		return ghostResponse;
	}

	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	public GhostsResponse getAllLocations() {

		GhostsResponse ghostsResponse = new GhostsResponse();

		List<Ghost> ghosts = ghostRepository.getAllGhosts();

		if(ghosts != null) {
			for(Ghost ghost : ghosts) {

				GhostResponse ghostResponse = new GhostResponse();
				ghostResponse.setId(ghost.getId());
				ghostResponse.setLocation(ghost.getLocation());

				ghostsResponse.addGhostResponse(ghostResponse);
			}
		}

		return ghostsResponse;
	}

}