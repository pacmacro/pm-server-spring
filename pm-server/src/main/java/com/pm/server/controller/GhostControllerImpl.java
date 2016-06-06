package com.pm.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.Coordinate;
import com.pm.server.player.Ghost;
import com.pm.server.player.GhostRepository;

@RestController
@RequestMapping("/ghost")
public class GhostControllerImpl implements GhostController {

	@Autowired
	private GhostRepository ghostRepository;

	@Override
	@RequestMapping(
			value="/integer",
			method=RequestMethod.GET
	)
	public int getInteger() {
		return 0;
	}

	// Returns map of ghost id to location
	@Override
	@RequestMapping(
			value="/locations",
			method=RequestMethod.GET
	)
	public Map<Integer, Coordinate> getAllLocations() {

		List<Ghost> ghosts = ghostRepository.getAllGhosts();

		Map<Integer, Coordinate> ghostsLocations =
				new HashMap<Integer, Coordinate>();

		for(Ghost ghost : ghosts) {
			ghostsLocations.put(ghost.getId(), ghost.getLocation());
		}

		return ghostsLocations;
	}

}