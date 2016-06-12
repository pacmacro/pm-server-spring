package com.pm.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController implements Controller {

	@RequestMapping(
			value="/",
			method=RequestMethod.GET)
	public String home() {
		return "Welcome to the Pac Macro server!";
	}

}
