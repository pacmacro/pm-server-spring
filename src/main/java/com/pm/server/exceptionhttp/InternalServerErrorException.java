package com.pm.server.exceptionhttp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public final class InternalServerErrorException extends Exception {

	private static final long serialVersionUID = -6386399765634671886L;

	public InternalServerErrorException(String message) {
		super(message);
	}

}
