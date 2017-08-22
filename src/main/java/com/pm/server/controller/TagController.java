package com.pm.server.controller;

import com.pm.server.PmServerException;
import com.pm.server.datatype.Player;
import com.pm.server.manager.TagManager;
import com.pm.server.request.TagRequest;
import com.pm.server.utils.JsonUtils;
import com.pm.server.utils.ValidationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {

	private TagManager tagManager;

	private final static Logger log =
			LogManager.getLogger(TagController.class.getName());

	@Autowired
	public TagController(TagManager tagManager) {
		this.tagManager = tagManager;
	}

	@RequestMapping(
			value="/{reporter}",
			method=RequestMethod.POST,
			produces={ "application/json" }
	)
	@SuppressWarnings("rawtypes")
	public ResponseEntity registerTag(
			@PathVariable String reporter,
			@RequestBody(required=false) TagRequest requestBody
	) throws PmServerException {
		log.info("Mapped POST /tag/{}", reporter);
		log.info("Request body: {}", JsonUtils.objectToJson(requestBody));

		if(requestBody == null) {
			throw new PmServerException(
					HttpStatus.BAD_REQUEST,
					"Either source or destination must be sent in the " +
					"request body."
			);
		}

		Player.Name reporterPlayer =
				ValidationUtils.validateRequestWithName(reporter);
		Player.Name sourcePlayer = ValidationUtils
				.validateTagSourceDestination(requestBody.getSource());
		Player.Name destinationPlayer = ValidationUtils
				.validateTagSourceDestination(requestBody.getDestination());

		tagManager.registerTag(reporterPlayer, sourcePlayer, destinationPlayer);

		log.info("Tag submitted.");
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

}
