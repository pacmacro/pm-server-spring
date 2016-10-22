package com.pm.server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(value = {PmServerException.class})
	public ResponseEntity<PmErrorResponse>
	pmServerExceptionHandler(PmServerException e) {

		PmErrorResponse response = new PmErrorResponse();

		response.setTimestamp(System.currentTimeMillis() / 1000L);
		response.setCode(e.getStatus().value());
		response.setPhrase(e.getStatus().getReasonPhrase());
		response.setMessage(e.getMessage());

		return ResponseEntity.status(e.getStatus()).body(response);
	}

}
