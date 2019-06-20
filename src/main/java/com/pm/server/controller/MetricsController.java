package com.pm.server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
@SuppressWarnings("unused")
public class MetricsController {

	private final static Logger log =
			LogManager.getLogger(MetricsController.class.getName());

	@RequestMapping(
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity getMetrics() {
		log.info("Mapped GET /metrics");

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
