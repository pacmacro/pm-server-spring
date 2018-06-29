package com.pm.server.controller;

import com.pm.server.manager.PacdotManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private PacdotManager pacdotManager;

	private final static Logger log =
			LogManager.getLogger(AdminController.class.getName());

	@RequestMapping(
			value="/pacdots/reset",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity resetPacdots() {
		log.info("Mapped POST /admin/pacdots/reset");

		pacdotManager.resetPacdots();
		log.info("All pacdots reset.");

		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

}
