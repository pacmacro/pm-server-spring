package com.pm.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ghost")
public class GhostControllerImpl implements GhostController {

	@Override
	@RequestMapping(
			value="/integer",
			method=RequestMethod.GET
	)
	public int getInteger() {
		return 0;
	}

}
