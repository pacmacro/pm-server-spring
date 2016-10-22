package com.pm.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	private final static Logger log =
			LogManager.getLogger(CustomExceptionHandler.class.getName());

	@ExceptionHandler(value = {PmServerException.class})
	public ResponseEntity<PmErrorResponse>
	pmServerExceptionHandler(PmServerException e) {

		HttpStatus status = e.getStatus();
		String logMessage = 
				"HTTP " + status.value() + " " + status.getReasonPhrase() +
				": " + e.getMessage();

		if(status.is5xxServerError()) {
			log.error(logMessage);
		}
		else {
			log.debug(logMessage);
		}

		PmErrorResponse response = new PmErrorResponse();

		response.setTimestamp(System.currentTimeMillis() / 1000L);
		response.setCode(e.getStatus().value());
		response.setPhrase(e.getStatus().getReasonPhrase());
		response.setMessage(e.getMessage());

		return ResponseEntity.status(e.getStatus()).body(response);
	}

}
