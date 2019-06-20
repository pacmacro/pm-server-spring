package com.pm.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@SuppressWarnings("unused")
public class HomeController {

	private final static Logger log =
			LogManager.getLogger(HomeController.class.getName());

	@RequestMapping(
			value="/",
			method=RequestMethod.GET)
	public ResponseEntity<String> home() {
		log.info("Mapped GET /");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Welcome to the Pac Macro server!");
	}

}
