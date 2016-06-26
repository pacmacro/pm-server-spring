package com.pm.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.CoordinateImpl;
import com.pm.server.player.Pacman;
import com.pm.server.player.PacmanImpl;
import com.pm.server.player.PacmanRepository;

@RestController
@RequestMapping("/pacman")
public class PacmanController implements PlayerController {

	@Autowired
	private PacmanRepository pacmanRepository;

	private final static Logger log =
			LogManager.getLogger(PacmanController.class.getName());

	@RequestMapping(
			value = "/{latitude}/{longitude}",
			method = RequestMethod.POST
	)
	public void createPacman(
			@PathVariable double latitude,
			@PathVariable double longitude,
			HttpServletResponse response) {

		log.debug("Mapped POST /pacman/{}/{}", latitude, longitude);

		if(pacmanRepository.getPlayer() != null) {
			log.debug("A Pacman already exists.");
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return;
		}

		Pacman pacman = new PacmanImpl();
		pacman.setLocation(new CoordinateImpl(latitude, longitude));

		try {
			pacmanRepository.addPlayer(pacman);
		}
		catch(Exception e) {
			log.debug(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);

	}

	@RequestMapping(
			method=RequestMethod.DELETE
	)
	public void deletePacman(
			HttpServletResponse response) {

		log.debug("Mapped DELETE /pacman");

		if(pacmanRepository.getPlayer() == null) {
			log.warn("No Pacman exists.");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		pacmanRepository.clearPlayers();
		response.setStatus(HttpServletResponse.SC_OK);

	}

}
