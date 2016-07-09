package com.pm.server.exceptionhttp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public final class BadRequestException extends Exception {

	private static final long serialVersionUID = 2646060704659045403L;

	public BadRequestException(String message) {
		super(message);
	}

}
